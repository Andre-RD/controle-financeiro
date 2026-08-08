package br.com.controlefinanceiro.api;

import br.com.controlefinanceiro.api.dto.*;
import br.com.controlefinanceiro.domain.entity.*;

import java.util.List;

public final class ApiMapper {
    private ApiMapper() {
    }

    public static UsuarioResponse usuario(Usuario e) {
        return new UsuarioResponse(e.getId(), e.getNome(), e.getCreatedAt());
    }

    public static CategoriaResponse categoria(Categoria e) {
        return new CategoriaResponse(e.getId(), e.getNome(), e.getTipo());
    }

    public static FormaPagamentoResponse forma(FormaPagamento e) {
        return new FormaPagamentoResponse(e.getId(), e.getUsuario().getId(), e.getDescricao(), e.getTipo(), e.getCartao() == null ? null : e.getCartao().getId());
    }

    public static CartaoCreditoResponse cartao(CartaoCredito e) {
        return new CartaoCreditoResponse(e.getId(), e.getFormaPagamento().getId(), e.getDiaFechamento(), e.getDiaVencimento(), e.getLimite());
    }

    public static ParcelaResponse parcela(Parcela e) {
        return new ParcelaResponse(e.getId(), e.getNumeroParcela(), e.getValorParcela(), e.getFatura() == null ? null : e.getFatura().getId(), e.getDataVencimento(), e.getStatus());
    }

    public static CompraResponse compra(Compra e, List<Parcela> parcelas) {
        return new CompraResponse(
                e.getId(),
                e.getUsuario().getId(),
                e.getCategoria().getId(),
                e.getFormaPagamento().getId(),
                e.getGastoFixo() == null ? null : e.getGastoFixo().getId(),
                e.getDescricao(),
                e.getValorTotal(),
                e.getDataCompra(),
                e.getNumParcelas(),
                e.getStatus(),
                parcelas.stream().map(ApiMapper::parcela).toList());
    }

    public static FaturaResponse fatura(Fatura e, java.math.BigDecimal total, List<Parcela> parcelas) {
        return new FaturaResponse(e.getId(), e.getCartao().getId(), e.getMesReferencia(), e.getDataFechamento(), e.getDataVencimento(), e.getStatus(), total, parcelas.stream().map(ApiMapper::parcela).toList());
    }

    public static GastoFixoResponse gasto(GastoFixo e) {
        return new GastoFixoResponse(e.getId(), e.getUsuario().getId(), e.getCategoria().getId(), e.getFormaPagamento().getId(), e.getDescricao(), e.getValorPrevisto(), e.getDiaVencimento(), e.getRecorrencia(), e.getDataInicio(), e.getDataFim(), e.isAtivo());
    }
}
