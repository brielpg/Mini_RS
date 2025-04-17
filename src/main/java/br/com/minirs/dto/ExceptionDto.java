package br.com.minirs.dto;

import org.springframework.http.HttpStatus;

public record ExceptionDto(
        Integer code,
        HttpStatus status,
        String message
) {
}
