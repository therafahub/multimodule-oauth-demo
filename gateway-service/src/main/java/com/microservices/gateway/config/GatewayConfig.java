package com.microservices.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de rutas del Gateway
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            // Auth Service
            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("http://localhost:8081"))
            
            // User Service
            .route("user-service", r -> r
                .path("/users/**")
                .uri("http://localhost:8082"))
            
            // Web UI
            .route("web-ui", r -> r
                .path("/ui/**", "/", "/index.html")
                .uri("http://localhost:8080"))
            
            .build();
    }
}
