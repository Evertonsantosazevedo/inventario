package br.edu.ifg.luziania.model.dto;

import br.edu.ifg.luziania.model.entity.Perfil;

public record UsuarioListDTO(
        Long id,
        String nome,
        String email,
        Perfil perfil
) {}

