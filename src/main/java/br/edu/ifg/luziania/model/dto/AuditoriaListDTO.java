package br.edu.ifg.luziania.model.dto;

import java.time.LocalDateTime;

public record AuditoriaListDTO(
        Long id,
        String nomeUsuario,
        String emailUsuario,
        String acao,
        LocalDateTime dataHora
) {
}
