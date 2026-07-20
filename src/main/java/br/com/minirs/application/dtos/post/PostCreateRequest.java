package br.com.minirs.application.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest(
        @NotNull(message = "User Id is required")
        Long userId,
        @NotBlank(message = "Post content is required")
        String content
) {
}
