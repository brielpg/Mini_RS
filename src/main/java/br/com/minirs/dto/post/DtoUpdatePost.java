package br.com.minirs.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoUpdatePost(
        @NotNull(message = "Post Id é obrigatório")
        Long postId,
        @NotBlank(message = "Post Content é obrigatório")
        String content
) {
}
