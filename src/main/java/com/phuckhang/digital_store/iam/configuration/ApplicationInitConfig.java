package com.phuckhang.digital_store.iam.configuration;

import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.enums.Role;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                HashSet<Role> roles = new HashSet<>();
                roles.add(Role.ADMIN);

                User user = User.builder()
                        .username("admin")
                        .fullName("Admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .isActive(true)
                        .build();
                userRepository.save(user);
                log.warn("Admin has been created");
            }
        };
    };
}
