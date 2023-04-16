package com.quanht.controller;

import com.quanht.request.LoginRequest;
import com.quanht.request.RegisterRequest;
import com.quanht.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/ecommerce/api/v1/auth/login")
    @ResponseBody
    public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return authService.login(loginRequest, response);
    }

    @GetMapping("/admin/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        session.invalidate();

        SecurityContextHolder.getContext().setAuthentication(null);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_COOKIE")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        return "redirect:/admin";
    }

    @PostMapping(value = "/ecommerce/api/v1/client/auth/login")
    @ResponseBody
    public String loginClient(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return authService.loginClient(loginRequest, response);
    }

    @GetMapping("/client/logout")
    public String logoutClient(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        session.invalidate();

        SecurityContextHolder.getContext().setAuthentication(null);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_COOKIE_CLIENT")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        return "redirect:/shop";
    }

    @PostMapping("/ecommerce/api/v1/client/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/ecommerce/api/v1/client/auth/confirm")
    public String confirm(@RequestParam("token") String token) {
        try {
            authService.confirmToken(token);
            return "/web/confirm-token";
        } catch (Exception e) {
            return "/web/resend-token";
        }
    }

    @PostMapping("/ecommerce/api/v1/client/auth/resend-token")
    public ResponseEntity<?> resendToken(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.generateTokenAndSendMail(request.getEmail(), request.getBaseUrl()));
    }

    @GetMapping("/ecommerce/api/v1/client/auth/reset-password/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        authService.resetPasswordAndSendMail(email);
        return ResponseEntity.noContent().build();
    }

}
