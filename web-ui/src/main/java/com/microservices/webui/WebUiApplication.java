package com.microservices.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación Web UI
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.microservices.webui",
    "com.microservices.common"
})
public class WebUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebUiApplication.class, args);
    }
}
