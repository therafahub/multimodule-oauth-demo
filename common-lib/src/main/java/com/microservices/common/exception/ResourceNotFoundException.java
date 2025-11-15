package com.microservices.common.exception;

import java.io.Serial;

/**
 * Excepción para recursos no encontrados
 */
public class ResourceNotFoundException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String resourceName;
    private final Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(String.format("%s not found with identifier: %s", resourceName, identifier));
        this.resourceName = resourceName;
        this.identifier = identifier;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getIdentifier() {
        return identifier;
    }
}
