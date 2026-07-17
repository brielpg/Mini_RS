package br.com.minirs.application.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoCreatePost(
        @NotNull(message = "User é obrigatório")
        Long userId,
        @NotBlank(message = "Content é obrigatório")
        String content
) {
}
