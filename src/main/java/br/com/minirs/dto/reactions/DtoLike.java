package br.com.minirs.dto.reactions;

import jakarta.validation.constraints.NotNull;

public record DtoLike(
        @NotNull(message = "User Id é obrigatório")
        Long userId,
        @NotNull(message = "Post Id é obrigatório")
        Long postId
) {
}
