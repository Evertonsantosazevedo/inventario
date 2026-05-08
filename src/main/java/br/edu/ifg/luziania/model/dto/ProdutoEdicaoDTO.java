package br.edu.ifg.luziania.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoEdicaoDTO(
        @NotBlank
        String codigo,
        @NotBlank
        String nome,
        @NotBlank
        String marca,
        @NotNull
        @Positive
        BigDecimal valorVenda
) {

}
