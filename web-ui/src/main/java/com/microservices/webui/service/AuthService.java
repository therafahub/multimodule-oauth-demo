package com.microservices.webui.service;

import com.microservices.webui.controller.AuthController.RegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Servicio para comunicación con Auth Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient webClient;

    public void register(RegisterDto request) {
        log.info("Llamando auth-service para registrar usuario");
        
        webClient.post()
            .uri("http://localhost:8081/auth/api/v1/auth/register")
            .bodyValue(request)
            .retrieve()
            .toEntity(String.class)
            .block();
    }
}
