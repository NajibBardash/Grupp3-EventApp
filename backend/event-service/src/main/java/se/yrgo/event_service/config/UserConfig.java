package se.yrgo.event_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * This class generates two dummy users, one as admin and one as user, they match the dummy-users in user-service.
 * With this we can now try and log in as an admin
 */
@Configuration
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}secret")
                .roles("USER", "ADMIN")
                .build();

        UserDetails testUser = User.withUsername("testuser")
                .password("{noop}password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user, testUser);
    }
}