package br.com.minirs.application.dtos.reactions;

import jakarta.validation.constraints.NotNull;

public record DtoLike(
        @NotNull(message = "User Id é obrigatório")
        Long userId,
        @NotNull(message = "Post Id é obrigatório")
        Long postId
) {
}
