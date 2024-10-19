package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private static final List<String> PATHS_NO_AUTH = List.of("/*/login", "/users/**");

    @Bean
    public UserDetailsService UserDetailsService() {
        return new ShopmeUserDetailsService();
    }

	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity, HttpServletRequest request)
            throws Exception {

        String[] pathsNoAuth = new String[PATHS_NO_AUTH.size()];
        PATHS_NO_AUTH.toArray(pathsNoAuth);

        System.out.println("pathsNoAuth" + Arrays.toString(pathsNoAuth));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(pathsNoAuth).permitAll() // Cho phép truy cập không cần xác thực
                        .anyRequest().authenticated() // Tất cả yêu cầu khác cần xác thực
                );
        return httpSecurity.build();
    }
}
