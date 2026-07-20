package br.com.minirs.application.dtos.post;

import br.com.minirs.application.dtos.user.UserResponse;

import java.time.LocalDateTime;

public record PostResponse (
        Long id,
        String content,
        UserResponse postOwner,
        Integer likeCount,
        LocalDateTime publishedAt
) {
}
