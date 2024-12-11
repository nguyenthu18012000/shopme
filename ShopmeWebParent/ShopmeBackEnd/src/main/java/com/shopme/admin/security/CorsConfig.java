package com.shopme.admin.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cho phép tất cả các đường dẫn
                .allowedOrigins("*") // Cho phép tất cả các origin (nên thay thế bằng các origin cụ thể trong môi trường sản xuất)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*"); // Cho phép tất cả các header
    }
}