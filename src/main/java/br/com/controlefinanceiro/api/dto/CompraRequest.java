package br.com.controlefinanceiro.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraRequest(@NotNull @Positive Long usuarioId, @NotNull @Positive Long categoriaId,
                            @NotNull @Positive Long formaPagamentoId, @Positive Long gastoFixoId,
                            @NotBlank @Size(max = 500) String descricao, @NotNull @Positive BigDecimal valorTotal,
                            @NotNull LocalDate dataCompra, @NotNull @Positive Integer numParcelas) {
}
