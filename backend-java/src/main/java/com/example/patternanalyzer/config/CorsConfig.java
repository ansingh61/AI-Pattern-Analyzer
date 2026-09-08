package com.example.patternanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.path}")
    private String path;
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;
    @Value("${app.cors.allowed-methods}")
    private String allowedMethods;
    @Value("${app.cors.allowed-headers}")
    private String allowedHeaders;

    private String[] split(String value) {
        if (value == null || value.isBlank()) {
            return new String[0];
        }
        return value.split("\\s*,\\s*");
    }

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(path)
            .allowedOrigins(split(allowedOrigins))
            .allowedMethods(split(allowedMethods))
            .allowedHeaders(split(allowedHeaders));
    }
}