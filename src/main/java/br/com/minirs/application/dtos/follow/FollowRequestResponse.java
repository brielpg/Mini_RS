package br.com.minirs.application.dtos.follow;

import br.com.minirs.application.dtos.user.UserResponse;

import java.time.LocalDateTime;

public record FollowRequestResponse (
        Long id,
        UserResponse requester,
        UserResponse requested,
        LocalDateTime requestDate
        ) {
}
