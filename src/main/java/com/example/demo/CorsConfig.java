package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 允许所有路径
                        .allowedOriginPatterns("*")  // 允许所有源
                        .allowedMethods("*")  // 允许所有方法
                        .allowedHeaders("*")  // 允许所有请求头
                        .allowCredentials(false)  // 允许凭证
                        .maxAge(3600);  // 预检请求的有效期
            }
        };
    }
}