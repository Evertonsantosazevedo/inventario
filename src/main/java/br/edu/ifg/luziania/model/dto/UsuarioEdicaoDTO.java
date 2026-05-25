package br.edu.ifg.luziania.model.dto;

import br.edu.ifg.luziania.model.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioEdicaoDTO(
        @NotBlank
        String nome,
        @Email
        @NotBlank(message = "e-mail não ficar vazio")
        String email,
        Perfil perfil
) {
}
