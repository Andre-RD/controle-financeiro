package br.com.controlefinanceiro.api.dto;

import java.math.BigDecimal;

public record CartaoCreditoResponse(Long id, Long formaPagamentoId, Integer diaFechamento, Integer diaVencimento,
                                    BigDecimal limite) {
}
