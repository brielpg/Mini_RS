package br.com.minirs.dto.reactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoUpdateComment(
        @NotNull(message = "Comment Id é obrigatório")
        Long commentId,
        @NotNull(message = "User Id é obrigatório")
        Long userId,
        @NotBlank(message = "Comment Content é obrigatório")
        String content
) {
}
