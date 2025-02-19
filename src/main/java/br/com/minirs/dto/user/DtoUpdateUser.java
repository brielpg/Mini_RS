package br.com.minirs.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DtoUpdateUser(
        @NotNull(message = "Id é obrigatório")
        Long id,
        String fullName,
        String userName,
        @Email
        String email,
        LocalDate birthDate,
        String password,
        String biography,
        String gender
) {
}
