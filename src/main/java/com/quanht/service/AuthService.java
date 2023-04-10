package com.quanht.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.quanht.entities.Account;
import com.quanht.entities.Role;
import com.quanht.entities.Token;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.AccountRepository;
import com.quanht.repositories.RoleRepository;
import com.quanht.repositories.TokenRepository;
import com.quanht.request.LoginRequest;
import com.quanht.request.RegisterRequest;
import com.quanht.security.ClientJwtUtils;
import com.quanht.security.JwtUtils;
import com.quanht.utils.Constant;
import com.quanht.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private ClientJwtUtils clientJwtUtils;
    private MailService mailService;

    @Autowired
    public AuthService(AccountRepository accountRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager,
                       TokenRepository tokenRepository, BCryptPasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, ClientJwtUtils clientJwtUtils, MailService mailService) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.clientJwtUtils = clientJwtUtils;
        this.mailService = mailService;
    }

    public String login(LoginRequest request, HttpServletResponse httpServletResponse) {
        try {
            // Tạo đối tượng xác thực dựa trên thông tin request
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Lưu trữ thông tin của đối tượng đã đăng nhập
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

            // Lưu thông tin vào trong cookie (nếu không sử dụng cookie thì trả thẳng token về
            // cho client và mỗi request client gửi lên đều phải kèm token trong header của request)
            Cookie cookie = new Cookie("JWT_COOKIE", token);
            cookie.setPath("/");
            cookie.setMaxAge(Constant.MAX_AGE_COOKIE); // Thời gian hết hạn cookie
            cookie.setHttpOnly(true); // Không cho phép client chỉnh sửa thông tin trong cookie (read-only)

            httpServletResponse.addCookie(cookie);

            // Trả về token cho client
            return token;
        } catch (Exception ex) {
            throw new BadRequestException("Email hoặc mật khẩu không chính xác");
        }
    }

    public String loginClient(LoginRequest request, HttpServletResponse httpServletResponse) {
        try {
            // Tạo đối tượng xác thực dựa trên thông tin request
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Lưu trữ thông tin của đối tượng đã đăng nhập
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = clientJwtUtils.generateToken((UserDetails) authentication.getPrincipal());

            // Lưu thông tin vào trong cookie (nếu không sử dụng cookie thì trả thẳng token về
            // cho client và mỗi request client gửi lên đều phải kèm token trong header của request)
            Cookie cookie = new Cookie("JWT_COOKIE_CLIENT", token);
            cookie.setPath("/");
            cookie.setMaxAge(Constant.MAX_AGE_COOKIE); // Thời gian hết hạn cookie
            cookie.setHttpOnly(true); // Không cho phép client chỉnh sửa thông tin trong cookie (read-only)

            httpServletResponse.addCookie(cookie);

            // Trả về token cho client
            return token;
        } catch (Exception ex) {
            Optional<Account> account = accountRepository.findByEmail(request.getEmail());
            if (account.isPresent()) {
                throw new BadRequestException("Tài khoản của bạn chưa được xác thực");
            }
            throw new BadRequestException("Email hoặc mật khẩu không chính xác");
        }
    }

    // REGISTER USER
    @Transactional
    public Account register(RegisterRequest request) {
        // Lấy thông tin user dựa trên email
        Optional<Account> accountOptional = accountRepository.findByEmail(request.getEmail());

        if (accountOptional.isPresent()) {
            // Nếu user được tìm thấy có trùng các thuộc tính và chưa được kích hoạt
            // Gửi email kích hoạt
            Account account = accountOptional.get();
            if (!account.getEnabled()
                    && account.getName().equals(request.getName())
                    && account.getEmail().equals(request.getEmail())) {
                return generateTokenAndSendMail(account.getEmail());
            }

            throw new BadRequestException("Email = " + request.getEmail() + " đã tồn tại");
        }

        // Mã hóa password
        String passwordEncode = passwordEncoder.encode(request.getPassword());

        // Tạo user và lưu vào database
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("CUSTOMER").get());

        Account newAccount = Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncode)
                .enabled(false)
                .roles(roles)
                .build();
        accountRepository.save(newAccount);

        // Sinh ra token
        return generateTokenAndSendMail(newAccount.getEmail());
    }

    // SINH TOKEN - SEND MAIL
    public Account generateTokenAndSendMail(String email) {
        Account account = accountRepository.findByEmail(email).get();

        // Sinh ra token
        String tokenString = UUID.randomUUID().toString();

        // Tạo token và lưu token
        Token token = new Token(
                tokenString,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account
        );
        tokenRepository.save(token);

        // Gửi email
        String link = "http://localhost:8080/ecommerce/api/v1/client/auth/confirm?token=" + tokenString;
        try {
            mailService.sendEmail(account.getEmail(), "Xác thực tài khoản", link);
        } catch (MailjetSocketTimeoutException | MailjetException e) {
            throw new RuntimeException(e);
        }

        return account;

    }

    // VERIFY TOKEN
    public String confirmToken(String tokenString) {
        // Lấy thông tin của token
        Token token = tokenRepository.findByToken(tokenString).orElseThrow(() ->
                new NotFoundException("Không tìm thấy token")
        );

        // Xem token đã được confirm hay chưa
        if (token.getConfirmedAt() != null) {
            throw new BadRequestException("Token đã được xác thực");
        }

        // Xem token đã hết hạn chưa
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token đã hết thời gian");
        }

        // Active token
        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);

        // Active user
        Account account = token.getAccount();
        account.setEnabled(true);
        accountRepository.save(account);

        return "confirmed";
    }

    public void resetPasswordAndSendMail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> {
            throw new BadRequestException("Email này chưa được đăng ký tài khoản");
        });

        String newPassword = PasswordGenerator.generatePassword();
        // Mã hóa password
        String passwordEncode = passwordEncoder.encode(newPassword);

        account.setPassword(passwordEncode);
        accountRepository.save(account);

        // Gửi email
        try {
            mailService.sendResetPasswordMail(email, "Reset mật khẩu", newPassword);
        } catch (MailjetSocketTimeoutException | MailjetException e) {
            throw new RuntimeException(e);
        }
    }

    public String logout(HttpSession session) {
        session.invalidate();
        return "logout success";
    }

}
