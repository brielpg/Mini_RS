package br.com.minirs.application.dtos.follow;

import jakarta.validation.constraints.NotNull;

public record FollowRequestRequest(
        @NotNull(message = "Requester Id is required")
        Long requesterId,
        @NotNull(message = "Requested Id is required")
        Long requestedId
) {
}
