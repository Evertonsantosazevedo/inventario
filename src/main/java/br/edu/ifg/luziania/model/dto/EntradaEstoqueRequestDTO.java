package br.edu.ifg.luziania.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EntradaEstoqueRequestDTO(
        @NotNull
        @Positive
        Integer quantidade
) {
}
