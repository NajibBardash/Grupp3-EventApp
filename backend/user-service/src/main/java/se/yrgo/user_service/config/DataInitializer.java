package se.yrgo.user_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.user_service.data.UserDao;
import se.yrgo.user_service.domain.Role;
import se.yrgo.user_service.domain.User;

import java.time.LocalDate;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserDao userDao, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if default user already exists
            Optional<User> existingUser = userDao.getUserByUsername("user");

            if (existingUser.isEmpty()) {
                // Create default user
                User defaultUser = new User();
                defaultUser.setCustomerId("CUST-001");
                defaultUser.setUsername("user");
                defaultUser.setName("Default User");
                defaultUser.setEmail("user@example.com");
                defaultUser.setPassword(passwordEncoder.encode("secret"));
                defaultUser.setBirthdate(LocalDate.of(1990, 1, 1));
                defaultUser.getRoles().add(Role.USER);
                defaultUser.getRoles().add(Role.ADMIN);

                userDao.save(defaultUser);
                System.out.println("Created default user: username=user, password=secret");
            } else {
                System.out.println("Default user already exists");
            }

            // Create additional test user
            Optional<User> testUser = userDao.getUserByUsername("testuser");

            if (testUser.isEmpty()) {
                User test = new User();
                test.setCustomerId("CUST-002");
                test.setUsername("testuser");
                test.setName("Test User");
                test.setEmail("test@example.com");
                test.setPassword(passwordEncoder.encode("password"));
                test.setBirthdate(LocalDate.of(1995, 5, 15));
                test.getRoles().add(Role.USER);

                userDao.save(test);
                System.out.println("Created test user: username=testuser, password=password");
            } else {
                System.out.println("Test user already exists");
            }
        };
    }
}
