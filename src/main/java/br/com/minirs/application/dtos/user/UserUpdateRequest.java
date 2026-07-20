package br.com.minirs.application.dtos.user;

import br.com.minirs.domain.enums.PrivacyStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record UserUpdateRequest(
        @NotNull(message = "Id is required")
        Long id,
        String fullName,
        String userName,
        @Email(message = "Invalid email address")
        String email,
        @Past(message = "Birth date must be in the past")
        LocalDateTime birthDate,
        String password,
        String biography,
        PrivacyStatusEnum profilePrivacyStatus
) {
}
