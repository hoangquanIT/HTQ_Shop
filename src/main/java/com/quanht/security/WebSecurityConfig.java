package com.quanht.security;

import com.quanht.service.AccountService;
import lombok.AllArgsConstructor;
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
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;
    private final AuthenticationEntryPointCustom authenticationEntryPointCustom;
    private final AuthorizationFilterCustom authorizationFilterCustom;
    private final AccessDeniedHandlerCustom accessDeniedHandlerCustom;

    private final ClientAuthenticationEntryPointCustom clientAuthenticationEntryPointCustom;
    private final ClientAccessDeniedHandlerCustom clientAccessDeniedHandlerCustom;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/ecommerce/api/v1/auth/**").permitAll()
                .antMatchers("/admin/css/**", "/admin/custom/**", "/admin/img/**",
                        "/admin/js/**", "/admin/scss/**", "/admin/vendor/**", "/ecommerce/api/v1/client/**").permitAll()
                .antMatchers("/admin/roles", "/admin/employees", "/admin/employee/**",
                        "/ecommerce/api/v1/employee/**", "/ecommerce/api/v1/roles/**")
                .hasRole("ADMIN")
                .antMatchers("/admin/**", "/ecommerce/api/v1/**", "/ecommerce/api/v1/employee/**")
                .hasAnyRole("ADMIN", "EMPLOYEE")
//                .antMatchers("/admin/products/**", "/admin/product/**", "/api/v1/**",
//                        "/admin/categories/**", "/admin/variants", "/admin/detail/**")
//                .hasAnyRole("INVENTORY", "ORDER", "CUSTOMER_CARE", "ADMIN")
//                .antMatchers("/admin/orders/**", "/admin/order/**",
//                        "/admin/customer/**", "/admin/customers/**")
//                .hasAnyRole( "ORDER", "CUSTOMER_CARE", "ADMIN")
//                .antMatchers("/admin/**")
//                .hasRole("ADMIN")
                //.anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/admin")
//                .defaultSuccessUrl("/admin/dashboard")
//                .failureUrl("/admin?error")
//                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointCustom)
                .accessDeniedHandler(accessDeniedHandlerCustom)
//                .accessDeniedPage("/admin/unauthorized")
                .and()
                .logout()
                    //.logoutUrl("/admin/logout")
                //.logoutSuccessUrl("/admin")
                .invalidateHttpSession(true)
                .deleteCookies("JWT_COOKIE", "JSESSIONID")
//                    .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .logoutSuccessUrl("/admin")
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationFilterCustom, UsernamePasswordAuthenticationFilter.class);
    }
}
