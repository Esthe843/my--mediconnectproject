package com.mediconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static frontend files
        registry.addResourceHandler("/frontend/**")
                .addResourceLocations("classpath:/static/frontend/");
        
        // Serve root index.html
        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/static/frontend/html/index.html");
        
        // Serve HTML files without extension
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/frontend/html/");
    }
}
