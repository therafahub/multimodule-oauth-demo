package com.microservices.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response genérico para OK
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    private T data;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    private int status;

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .code("SUCCESS")
                .message(message)
                .data(data)
                .status(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .code("CREATED")
                .message("Resource created successfully")
                .data(data)
                .status(201)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
