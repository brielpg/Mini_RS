package br.com.minirs.application.dtos.user;

import br.com.minirs.domain.enums.PrivacyStatusEnum;

import java.time.LocalDateTime;

public record UserResponse (
        Long id,
        String fullName,
        String userName,
        String email,
        LocalDateTime birthDate,
        PrivacyStatusEnum profilePrivacyStatus,
        Integer followersCount,
        Integer followingCount,
        Integer postCount,
        LocalDateTime createdAt
) {
}
