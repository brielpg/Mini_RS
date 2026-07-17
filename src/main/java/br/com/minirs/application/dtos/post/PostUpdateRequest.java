package br.com.minirs.application.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotNull(message = "Post Id é obrigatório")
        Long postId,
        @NotNull(message = "User Id é obrigatório")
        Long userId,
        @NotBlank(message = "Post Content é obrigatório")
        String content
) {
}
