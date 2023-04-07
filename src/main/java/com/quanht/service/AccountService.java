package com.quanht.service;

import com.quanht.dto.AccountDto;
import com.quanht.dto.CustomerDto;
import com.quanht.entities.Account;
import com.quanht.entities.Cart;
import com.quanht.entities.Role;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.AccountRepository;
import com.quanht.repositories.CartRepository;
import com.quanht.repositories.RoleRepository;
import com.quanht.request.*;
import com.quanht.security.ClientJwtUtils;
import com.quanht.security.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    private AccountRepository accountRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private JwtUtils jwtUtils;

    private ClientJwtUtils clientJwtUtils;
    private CartRepository cartRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, @Lazy BCryptPasswordEncoder passwordEncoder,
                          RoleRepository roleRepository, JwtUtils jwtUtils, ClientJwtUtils clientJwtUtils,
                          CartRepository cartRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.clientJwtUtils = clientJwtUtils;
        this.cartRepository = cartRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email).orElseThrow(() -> {
            throw new UsernameNotFoundException("Not found user with email = " + email);
        });
    }

    public List<AccountDto> getAccounts(){
        return accountRepository.findAccountDtos();
    }

    public List<CustomerDto> getCustomers(){
        return accountRepository.findCustomerDtos();
    }

    public Account getDetail(HttpServletRequest request){
        // Lấy token từ trong header của request
        String token = jwtUtils.getTokenFromCookie(request);

        // Parse thông tin từ token
        Claims claims = jwtUtils.getClaimsFromToken(token);

        // Lấy username (email khách hàng)
        String userName = claims.getSubject();

        // Lấy thông tin khách hàng qua email
        return accountRepository.findByEmail(userName).orElseThrow(() -> {
            throw new NotFoundException("Không tồn tại tài khoản có email " + userName);
        });
    }

    public Account getClientDetail(HttpServletRequest request){
        // Lấy token từ trong header của request
        String token = clientJwtUtils.getTokenFromCookie(request);

        // Parse thông tin từ token
        Claims claims = clientJwtUtils.getClaimsFromToken(token);

        // Lấy username (email khách hàng)
        String userName = claims.getSubject();

        // Lấy thông tin khách hàng qua email
        return accountRepository.findByEmail(userName).orElseThrow(() -> {
            throw new NotFoundException("Không tồn tại tài khoản có email " + userName);
        });
    }

    public Cart getClientCartNumbers(HttpServletRequest request){
        // Lấy token từ trong header của request
        String token = clientJwtUtils.getTokenFromCookie(request);

        // Parse thông tin từ token
        Claims claims = clientJwtUtils.getClaimsFromToken(token);

        // Lấy username (email khách hàng)
        String userName = claims.getSubject();

        System.out.println(userName);

        // Lấy thông tin khách hàng qua email
        Optional<Account> account = accountRepository.findByEmail(userName);

        System.out.println(account.get().getCurrentCartId());

        if (account.get().getCurrentCartId() != null) {
            return cartRepository.findById(account.get().getCurrentCartId()).orElseThrow(() -> {
                throw new NotFoundException("Không tìm thấy giỏ hàng");
            });
        } else {
            return new Cart();
        }
    }

    @Transactional
    public Account updateInfo(UpdateInfoRequest updateInfo){
        Account account = accountRepository.findById(updateInfo.getId()).orElseThrow(() -> {
            throw new NotFoundException("Không tìm thấy tài khoản");
        });

        Optional<Account> oldAccount = accountRepository.findByPhone(updateInfo.getPhone());
        if (oldAccount.isPresent()){
            if (oldAccount.get().getId() != updateInfo.getId()){
                throw new BadRequestException("Số điện thoại đã tồn tại đã tồn tại");
            }
        }


        account.setName(updateInfo.getName());
        account.setPhone(updateInfo.getPhone());

        return account;
    }

    @Transactional
    public Account updatePassword(UpdatePasswordRequest updatePass, HttpServletRequest request){
        Account account = getDetail(request);

        String oldPassword = updatePass.getOldPassword();
        String newPassword = updatePass.getNewPassword();

        if (passwordEncoder.matches(oldPassword, account.getPassword())){
            account.setPassword(passwordEncoder.encode(newPassword));
            return account;
        } else {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }

    }

    @Transactional
    public Account updateClientPassword(UpdatePasswordRequest updatePass, HttpServletRequest request){
        Account account = getClientDetail(request);

        String oldPassword = updatePass.getOldPassword();
        String newPassword = updatePass.getNewPassword();

        if (passwordEncoder.matches(oldPassword, account.getPassword())){
            account.setPassword(passwordEncoder.encode(newPassword));
            return account;
        } else {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }

    }

    @Transactional
    public Account createEmployee(EmployeeRequest employeeRequest){
        if (accountRepository.findByEmail(employeeRequest.getEmail()).isPresent()){
            throw new BadRequestException("Email đã tồn tại");
        }

        if (accountRepository.findByPhone(employeeRequest.getPhone()).isPresent()){
            throw new BadRequestException("Số điện thoại đã tồn tại");
        }

        // Mã hóa password
        String passwordEncode = passwordEncoder.encode(employeeRequest.getPassword());

        Account account = Account.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .phone(employeeRequest.getPhone())
                .password(passwordEncode)
                .enabled(true)
                .build();
        List<Role> roles = new ArrayList<>();
        employeeRequest.getRoles().forEach(id -> roles.add(roleRepository.findById(id).get()));
        account.setRoles(roles);
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id){
        return accountRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Không tìm thấy tài khoản");
        });
    }

    @Transactional
    public void updateEmployee(EmployeeUpdateRequest updateEmployee){
        Optional<Account> oldAccount = accountRepository.findByPhone(updateEmployee.getPhone());
        if (oldAccount.isPresent()){
            if (oldAccount.get().getId() != updateEmployee.getId()){
                throw new BadRequestException("Số điện thoại đã tồn tại đã tồn tại");
            }
        }

        accountRepository.updateEmployee(updateEmployee.getId(), updateEmployee.getName(), updateEmployee.getPhone(), LocalDateTime.now());
    }

    @Transactional
    public void deleteEmployee(Long id){
        accountRepository.deleteById(id);
    }

    @Transactional
    public Account createCustomer(CustomerRequest customerRequest){
        if (accountRepository.findByEmail(customerRequest.getEmail()).isPresent()){
            throw new BadRequestException("Email đã tồn tại");
        }

        if (accountRepository.findByPhone(customerRequest.getPhone()).isPresent()){
            throw new BadRequestException("Số điện thoại đã tồn tại");
        }

        // Mã hóa password
        String passwordEncode = passwordEncoder.encode(customerRequest.getPassword());

        Account account = Account.builder()
                .name(customerRequest.getName())
                .email(customerRequest.getEmail())
                .phone(customerRequest.getPhone())
                .password(passwordEncode)
                .address(customerRequest.getAddress())
                .cityCode(customerRequest.getCityCode())
                .enabled(true)
                .build();
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("CUSTOMER").get());
        account.setRoles(roles);
        return accountRepository.save(account);
    }

    @Transactional
    public void updateCustomer(CustomerUpdateRequest updateCustomer){
        Optional<Account> oldAccount = accountRepository.findByPhone(updateCustomer.getPhone());
        if (oldAccount.isPresent()){
            if (oldAccount.get().getId() != updateCustomer.getId()){
                throw new BadRequestException("Số điện thoại đã tồn tại đã tồn tại");
            }
        }

        accountRepository.updateCustomer(updateCustomer.getId(), updateCustomer.getName(), updateCustomer.getPhone(),
                updateCustomer.getAddress(), updateCustomer.getCityCode(), LocalDateTime.now());
    }

}
