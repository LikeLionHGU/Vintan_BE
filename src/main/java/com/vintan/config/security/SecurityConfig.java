package com.vintan.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration class for Spring Security.
 * Handles password encoding, CORS settings, and security filter chain.
 */
@Configuration
public class SecurityConfig {

    /**
     * Provides a PasswordEncoder bean using BCrypt hashing algorithm.
     * This is used for securely storing user passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the HTTP security filter chain.
     * - Disables CSRF (because it's often unnecessary for stateless APIs)
     * - Enables CORS with custom configuration
     * - Permits all requests (no authentication enforced)
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (useful for APIs)
                .csrf(AbstractHttpConfigurer::disable)
                // Apply custom CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Allow all requests without authentication
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**").permitAll()
                );
        return http.build();
    }

    /**
     * Defines a CORS configuration source.
     * - Allows specific frontend domains
     * - Enables credentials (cookies, authorization headers)
     * - Allows all headers and methods
     *
     * @return a configured UrlBasedCorsConfigurationSource instance
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",   // Local development
                "https://vin-tan.web.app/" // Deployed frontend
        ));
        configuration.setAllowCredentials(true); // Allow cookies and authorization headers
        configuration.addAllowedHeader("*");     // Allow all headers
        configuration.addAllowedMethod("*");     // Allow all HTTP methods (GET, POST, etc.)

        // Register CORS configuration for all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
