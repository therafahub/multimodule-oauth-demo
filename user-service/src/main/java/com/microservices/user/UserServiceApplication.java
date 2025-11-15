package com.microservices.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal del servicio de usuario
 */
@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {
    "com.microservices.user",
    "com.microservices.common"
})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
