package br.com.minirs.application.dtos.reactions;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull(message = "User Id is required")
        Long userId,
        @NotNull(message = "Post Id is required")
        Long postId
) {
}
