package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.Recorrencia;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoFixoRequest(@NotNull @Positive Long usuarioId, @NotNull @Positive Long categoriaId,
                               @NotNull @Positive Long formaPagamentoId, @NotBlank @Size(max = 500) String descricao,
                               @NotNull @Positive BigDecimal valorPrevisto,
                               @NotNull @Min(1) @Max(31) Integer diaVencimento, @NotNull Recorrencia recorrencia,
                               @NotNull LocalDate dataInicio, LocalDate dataFim, @NotNull Boolean ativo) {
}
