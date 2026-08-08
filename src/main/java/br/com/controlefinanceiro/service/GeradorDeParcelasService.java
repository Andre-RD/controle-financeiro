package br.com.controlefinanceiro.service;
import br.com.controlefinanceiro.api.exception.RegraDeNegocioException;
import br.com.controlefinanceiro.domain.entity.*;
import br.com.controlefinanceiro.domain.enums.*;
import br.com.controlefinanceiro.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class GeradorDeParcelasService {
    private final CompraRepository compras;
    private final CartaoCreditoRepository cartoes;
    private final FaturaRepository faturas;
    private final ParcelaRepository parcelas;
    public GeradorDeParcelasService(CompraRepository compras, CartaoCreditoRepository cartoes, FaturaRepository faturas, ParcelaRepository parcelas) {
        this.compras = compras;
        this.cartoes = cartoes;
        this.faturas = faturas;
        this.parcelas = parcelas;
    }
    @Transactional     public Compra criar(Compra compra) {
        TipoFormaPagamento tipo = compra.getFormaPagamento().getTipo();
        validarParcelamento(tipo, compra.getNumParcelas());
        Compra salva = compras.save(compra);
        List<Parcela> geradas = new ArrayList<>();
        BigDecimal base = compra.getValorTotal().divide(BigDecimal.valueOf(compra.getNumParcelas()), 2, RoundingMode.DOWN);
        BigDecimal acumulado = BigDecimal.ZERO;
        for (int numero = 1; numero <= compra.getNumParcelas(); numero++) {
            BigDecimal valor = numero == compra.getNumParcelas()                     ? compra.getValorTotal().subtract(acumulado) : base;
            acumulado = acumulado.add(valor);
            Parcela parcela = Parcela.builder().compra(salva).numeroParcela(numero).valorParcela(valor).build();
            preencherDestino(parcela, compra, tipo, numero);
            geradas.add(parcela);
        }
        parcelas.saveAll(geradas);
        salva.setParcelas(geradas);
        return salva;
    }
    private void validarParcelamento(TipoFormaPagamento tipo, int numeroParcelas) {
        if (tipo != TipoFormaPagamento.CREDITO && tipo != TipoFormaPagamento.BOLETO && tipo != TipoFormaPagamento.CREDIARIO && numeroParcelas > 1) {
            throw new RegraDeNegocioException("DINHEIRO, DEBITO e PIX aceitam somente uma parcela");
        }
    }
    private void preencherDestino(Parcela parcela, Compra compra, TipoFormaPagamento tipo, int numero) {
        if (tipo == TipoFormaPagamento.CREDITO) {
            CartaoCredito cartao = cartoes.findByFormaPagamentoId(compra.getFormaPagamento().getId())                     .orElseThrow(() -> new RegraDeNegocioException("Forma de pagamento CREDITO exige cartão cadastrado"));
            YearMonth referencia = mesDaFatura(compra.getDataCompra(), numero, cartao.getDiaFechamento());
            Fatura fatura = faturas.findByCartaoCreditoIdAndMesReferencia(cartao.getId(), referencia)                     .orElseGet(() -> faturas.save(novaFatura(cartao, referencia)));
            parcela.setFatura(fatura);
            parcela.setDataVencimento(fatura.getDataVencimento());
            parcela.setStatus(StatusParcela.PENDENTE);
        }
        else if (tipo == TipoFormaPagamento.BOLETO || tipo == TipoFormaPagamento.CREDIARIO) {
            parcela.setDataVencimento(compra.getDataCompra().plusDays((long) numero * 30));
            parcela.setStatus(StatusParcela.PENDENTE);
        }
        else {
            parcela.setDataVencimento(compra.getDataCompra());
            parcela.setStatus(StatusParcela.PAGA);
        }
    }
    static YearMonth mesDaFatura(LocalDate compra, int numeroParcela, int diaFechamento) {
        YearMonth referencia = YearMonth.from(compra).plusMonths(numeroParcela - 1L);
        if (compra.getDayOfMonth() > Math.min(diaFechamento, compra.lengthOfMonth())) referencia = referencia.plusMonths(1);
        return referencia;
    }
    private Fatura novaFatura(CartaoCredito cartao, YearMonth referencia) {
        LocalDate fechamento = referencia.atDay(Math.min(cartao.getDiaFechamento(), referencia.lengthOfMonth()));
        YearMonth mesVencimento = cartao.getDiaVencimento() <= cartao.getDiaFechamento() ? referencia.plusMonths(1) : referencia;
        LocalDate vencimento = mesVencimento.atDay(Math.min(cartao.getDiaVencimento(), mesVencimento.lengthOfMonth()));
        return Fatura.builder().cartao(cartao).mesReferencia(referencia).dataFechamento(fechamento).dataVencimento(vencimento).status(StatusFatura.ABERTA).build();
    }
}
