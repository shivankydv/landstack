package com.landstack.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Login is publicly accessible
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // USER and ADMIN can read data
                        .requestMatchers(HttpMethod.GET, "/api/v1/**")
                        .authenticated()

                        // Only ADMIN can create data
                        .requestMatchers(HttpMethod.POST, "/api/v1/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can update data
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can partially update data
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete data
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
                        .hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}