package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.StatusFatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record FaturaResponse(Long id, Long cartaoId, YearMonth mesReferencia, LocalDate dataFechamento,
                             LocalDate dataVencimento, StatusFatura status, BigDecimal valorTotal,
                             List<ParcelaResponse> parcelas) {
}
