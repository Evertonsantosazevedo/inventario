package br.edu.ifg.luziania.model.dto;

import java.math.BigDecimal;

public record ProdutoListDTO(
        Long id,
        String codigo,
        String nome,
        String marca,
        Integer quantidade,
        BigDecimal valorVenda

) {
}
