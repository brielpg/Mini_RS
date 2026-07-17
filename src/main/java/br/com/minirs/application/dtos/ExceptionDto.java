package br.com.minirs.application.dtos;

import org.springframework.http.HttpStatus;

public record ExceptionDto(
        Integer code,
        HttpStatus status,
        String message
) {
}
