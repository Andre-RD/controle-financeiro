package br.com.controlefinanceiro.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CartaoCreditoRequest(@NotNull @Positive Long formaPagamentoId,
                                   @NotNull @Min(1) @Max(31) Integer diaFechamento,
                                   @NotNull @Min(1) @Max(31) Integer diaVencimento, @Positive BigDecimal limite) {
}
