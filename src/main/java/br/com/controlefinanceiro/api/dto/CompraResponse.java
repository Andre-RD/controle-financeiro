package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.StatusCompra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CompraResponse(
        Long id,
        Long usuarioId,
        Long categoriaId,
        Long formaPagamentoId,
        Long gastoFixoId,
        String descricao,
        BigDecimal valorTotal,
        LocalDate dataCompra,
        Integer numParcelas,
        StatusCompra status,
        List<ParcelaResponse> parcelas) {
}
