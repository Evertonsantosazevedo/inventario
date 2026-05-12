package br.edu.ifg.luziania.model.dto;

import br.edu.ifg.luziania.model.entity.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoListDTO (
        Long id,
        String nomeProduto,
        String marcaProduto,
        String nomeUsuario,
        Integer quantidade,
        TipoMovimentacao tipoMovimentacao,
        LocalDateTime dataHora
){
}
