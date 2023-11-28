package com.example.androidprojectserver.config;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private FirebaseAuth firebaseAuth;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        System.out.println("시큐리티 설정~~~");
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/**").hasAuthority("USER") // USER 권한이 있는 사용자에게 모든 페이지 허용
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new FirebaseTokenFilter(userDetailsService, firebaseAuth),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
        System.out.println("시큐리티 끝~~~");
    }
}