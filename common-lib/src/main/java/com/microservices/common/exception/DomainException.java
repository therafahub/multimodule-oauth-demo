package com.microservices.common.exception;

import java.io.Serial;

/**
 * Excepción base para el dominio
 */
public class DomainException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
