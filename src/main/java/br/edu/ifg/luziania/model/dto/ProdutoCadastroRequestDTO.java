package br.edu.ifg.luziania.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoCadastroRequestDTO(
        @NotBlank
        String codigo,
        @NotBlank
        String nome,
        @NotBlank
        String marca,
        @NotNull
        @Min(0)
        Integer quantidade,
        @NotNull
        @Positive
        BigDecimal valorVenda

) {
}
