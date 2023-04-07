package com.quanht.security;

import com.quanht.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ClientAuthenticationEntryPointCustom clientAuthenticationEntryPointCustom;
    private final ClientAuthorizationFilterCustom clientAuthorizationFilterCustom;
    private final ClientAccessDeniedHandlerCustom clientAccessDeniedHandlerCustom;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                //.antMatchers("/ecommerce/api/v1/client/auth/**").permitAll()
                .antMatchers("/shop/account", "/ecommerce/api/v1/client/account").hasRole("CUSTOMER")
                //.anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/shop/login")
                .defaultSuccessUrl("/shop")
                .failureUrl("/shop/login?error")
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(clientAuthenticationEntryPointCustom)
                .accessDeniedHandler(clientAccessDeniedHandlerCustom)
                .and()
                .logout()
                .logoutUrl("/client/logout")
                .logoutSuccessUrl("/shop")
//                    .logoutUrl("/logout-handle")
                .invalidateHttpSession(true)
                .deleteCookies("JWT_COOKIE_CLIENT", "JSESSIONID")
//                    .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(clientAuthorizationFilterCustom, UsernamePasswordAuthenticationFilter.class);
    }

}
