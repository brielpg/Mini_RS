package br.com.minirs.application.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record UserCreateRequest(
        @NotBlank(message = "Full name is required")
        String fullName,
        @NotBlank(message = "Username is required")
        String userName,
        @Email(message = "Invalid email address")
        @NotBlank(message = "Email is required")
        String email,
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDateTime birthDate,
        @NotNull(message = "Password is required")
        String password
) {
}
