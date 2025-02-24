package br.com.minirs.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoCreatePost(
        @NotNull(message = "User é obrigatório")
        Long userId,
        @NotBlank(message = "Content é obrigatório")
        String content
) {
}
