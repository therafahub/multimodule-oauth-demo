package com.microservices.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del servicio de autenticación
 */
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {
    "com.microservices.auth",
    "com.microservices.common"
})
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
