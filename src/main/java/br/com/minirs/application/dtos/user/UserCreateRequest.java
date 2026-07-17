package br.com.minirs.application.dtos.user;

import br.com.minirs.domain.enums.PrivacyStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String fullName,
        @NotBlank(message = "Username é obrigatório")
        String userName,
        @Email(message = "Invalid Email")
        @NotBlank(message = "Email é obrigatório")
        String email,
        @NotNull(message = "Birth date é obrigatório")
        LocalDate birthDate,
        @NotNull(message = "Profile privacy é obrigatório")
        PrivacyStatusEnum profilePrivacyStatus,
        @NotNull(message = "Password é obrigatório")
        String password,
        String biography,
        String gender
) {
}
