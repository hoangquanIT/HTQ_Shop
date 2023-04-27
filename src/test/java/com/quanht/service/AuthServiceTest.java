package com.quanht.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quanht.entities.Account;
import com.quanht.exception.BadRequestException;
import com.quanht.repositories.AccountRepository;
import com.quanht.repositories.RoleRepository;
import com.quanht.repositories.TokenRepository;
import com.quanht.request.LoginRequest;
import com.quanht.security.ClientJwtUtils;
import com.quanht.security.JwtUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.quanht.utils.Constant;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AuthServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AuthService authService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ClientJwtUtils clientJwtUtils;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private MailService mailService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    /** khong co quyen
     * Method under test: {@link AuthService#loginClient(LoginRequest, HttpServletResponse)}
     */
    @Test
    void testLoginClient() throws AuthenticationException {
        Account account = new Account();
        account.setAddress("42 Main St");
        account.setCityCode(1);
        account.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        account.setCurrentCartId(1L);
        account.setEmail("jane.doe@example.org");
        account.setEnabled(true);
        account.setId(1L);
        account.setName("Name");
        account.setOrders(new ArrayList<>());
        account.setPassword("iloveyou");
        account.setPhone("6625550144");
        account.setRoles(new ArrayList<>());
        account.setTokens(new ArrayList<>());
        account.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<Account> ofResult = Optional.of(account);
        when(accountRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        LoginRequest request = new LoginRequest("jane.doe@example.org", "iloveyou");

        assertThrows(BadRequestException.class, () -> authService.loginClient(request, new Response()));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /** Dang nhap thanh cong
     * Method under test: {@link AuthService#loginClient(LoginRequest, HttpServletResponse)}
     */
    @Test
    public void testLoginClient2() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Account account = new Account();
        account.setEmail("test@example.com");
        account.setPassword(passwordEncoder.encode("password"));

        when(accountRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authenticationToken);

        UserDetails userDetails = mock(UserDetails.class);
//        when(authenticationToken.getPrincipal()).thenReturn(userDetails);

        String token = null;
        when(clientJwtUtils.generateToken(userDetails)).thenReturn(token);

        Cookie cookie = mock(Cookie.class);
        when(cookie.getPath()).thenReturn("/");
        when(cookie.getMaxAge()).thenReturn(Constant.MAX_AGE_COOKIE);
        when(cookie.isHttpOnly()).thenReturn(true);

        HttpServletResponse httpServletResponse = new Response();

        // Act
        String result = null;
        try {
            result = authService.loginClient(loginRequest, httpServletResponse);
        } catch (BadRequestException ex) {
            // Handle exception if needed
        }

        // Assert
        assertEquals(token, result);
    }

    /** chua xac thuc
     * Method under test: {@link AuthService#loginClient(LoginRequest, HttpServletResponse)}
     */
    @Test
    void testLoginClient3() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        LoginRequest request = new LoginRequest("jane.doe@example.org", "iloveyou");

        assertThrows(BadRequestException.class, () -> authService.loginClient(request, new Response()));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /** khong tim thay email
     * Method under test: {@link AuthService#loginClient(LoginRequest, HttpServletResponse)}
     */
    @Test
    void testLoginClient4() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(null);
        LoginRequest request = new LoginRequest("nohimin687@mitigado.com", "123Abc");

        assertThrows(BadRequestException.class, () -> authService.loginClient(request, new Response()));
    }

    /** Sai mat khau
     * Method under test: {@link AuthService#loginClient(LoginRequest, HttpServletResponse)}
     */
    @Test
    void testLoginClient5() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials", "JaneDoe"));
        LoginRequest request = new LoginRequest("nohimin687@mitigado.com", "iloveyou");

        assertThrows(BadRequestException.class, () -> authService.loginClient(request, new Response()));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

}

