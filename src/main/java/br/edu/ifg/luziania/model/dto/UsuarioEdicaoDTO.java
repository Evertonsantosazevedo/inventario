package br.edu.ifg.luziania.model.dto;

import br.edu.ifg.luziania.model.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioEdicaoDTO(
        @NotBlank
        String nome,
        @Email
        @NotBlank(message = "e-mail não ficar vazio")
        String email,
        Perfil perfil,
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha
) {
}
