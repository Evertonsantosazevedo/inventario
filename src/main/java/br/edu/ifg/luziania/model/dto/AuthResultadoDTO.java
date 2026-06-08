package br.edu.ifg.luziania.model.dto;

//Responsável pelo tráfego interno do token
public record AuthResultadoDTO(
        String token,
        String nome,
        String perfil
) {
}
