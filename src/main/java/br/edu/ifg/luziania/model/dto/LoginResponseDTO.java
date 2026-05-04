package br.edu.ifg.luziania.model.dto;

public record LoginResponseDTO(
        String token,
        String nome,
        String perfil) {
}
