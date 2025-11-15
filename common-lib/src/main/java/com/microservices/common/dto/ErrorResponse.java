package com.microservices.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response genérico para errores
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    @JsonProperty("error_description")
    private String errorDescription;

    private LocalDateTime timestamp;

    private String path;

    private int status;

    public static ErrorResponse of(String code, String message, int status) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
