package com.landstack.backend.config;

import com.landstack.backend.repository.UserRepository;
import com.landstack.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            UserService userService) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {
                userService.createUser(
                        "admin",
                        "admin123",
                        "ADMIN"
                );
            }

            if (!userRepository.existsByUsername("user")) {
                userService.createUser(
                        "user",
                        "user123",
                        "USER"
                );
            }
        };
    }
}