package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.TipoFormaPagamento;

public record FormaPagamentoResponse(Long id, Long usuarioId, String descricao, TipoFormaPagamento tipo,
                                     Long cartaoId) {
}
