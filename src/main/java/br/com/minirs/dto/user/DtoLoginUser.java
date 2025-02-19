package br.com.minirs.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DtoLoginUser(
        @Email
        @NotBlank(message = "Email é obrigatório")
        String email,
        @NotBlank(message = "Password é obrigatório")
        String password
) {
}
