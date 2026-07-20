package br.com.minirs.application.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotNull(message = "Post Id is required")
        Long postId,
        @NotBlank(message = "Post content is required")
        String content
) {
}
