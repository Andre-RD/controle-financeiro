package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.Recorrencia;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoFixoResponse(Long id, Long usuarioId, Long categoriaId, Long formaPagamentoId, String descricao,
                                BigDecimal valorPrevisto, Integer diaVencimento, Recorrencia recorrencia,
                                LocalDate dataInicio, LocalDate dataFim, boolean ativo) {
}
