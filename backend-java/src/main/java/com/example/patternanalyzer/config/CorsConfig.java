package com.example.patternanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final String path;
    private final List<String> allowedOrigins;
    private final List<String> allowedMethods;
    private final List<String> allowedHeaders;

    public CorsConfig(
            @Value("${app.cors.path}") String path,
            @Value("${app.cors.allowed-origins}") List<String> allowedOrigins,
            @Value("${app.cors.allowed-methods}") List<String> allowedMethods,
            @Value("${app.cors.allowed-headers}") List<String> allowedHeaders) {
        this.path = path;
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(path)
            .allowedOrigins(allowedOrigins.toArray(String[]::new))
            .allowedMethods(allowedMethods.toArray(String[]::new))
            .allowedHeaders(allowedHeaders.toArray(String[]::new));
    }
}