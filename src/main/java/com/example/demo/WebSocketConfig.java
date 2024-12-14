package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 允许所有地址的WebSocket连接
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // 允许所有源
                .withSockJS();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有源
        config.addAllowedOriginPattern("*");

        // 允许所有HTTP方法
        config.addAllowedMethod("*");

        // 允许所有头信息
        config.addAllowedHeader("*");

        // 允许携带认证信息
        config.setAllowCredentials(true);

        // 暴露所有头信息
        config.addExposedHeader("*");

        // 预检请求的有效期，单位为秒
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}