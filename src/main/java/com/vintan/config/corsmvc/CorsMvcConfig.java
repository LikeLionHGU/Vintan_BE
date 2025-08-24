package com.vintan.config.corsmvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for CORS (Cross-Origin Resource Sharing).
 * This allows specific frontend domains to access the backend API.
 */
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application.
     * This method specifies which origins, HTTP methods, and credentials are allowed.
     *
     * @param registry the CorsRegistry to configure allowed mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Apply CORS settings to all API endpoints
        registry.addMapping("/**")
                // Allow requests from specific frontend domains
                .allowedOrigins("https://vintan.netlify.app/", "http://localhost:3000/", "https://vin-tan.web.app/")
                // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                .allowedMethods("*")
                // Allow sending cookies and credentials across origins
                .allowCredentials(true)
                .allowCredentials(true); // Redundant but harmless
    }
}
