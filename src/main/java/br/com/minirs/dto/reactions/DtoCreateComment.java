package br.com.minirs.dto.reactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoCreateComment(
        @NotNull(message = "User Id é obrigatório")
        Long userId,
        @NotNull(message = "Post Id é obrigatório")
        Long postId,
        @NotBlank(message = "Post content é obrigatório")
        String content
) {
}
