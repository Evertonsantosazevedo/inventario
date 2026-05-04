package br.edu.ifg.luziania.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Email
        @NotBlank(message = "e-mail não ficar vazio")
        String email,
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @NotBlank(message = "A senha não pode ficar vazio")
        String senha

) {
}
