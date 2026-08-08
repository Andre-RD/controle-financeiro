package br.com.controlefinanceiro.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import br.com.controlefinanceiro.domain.entity.*;
import br.com.controlefinanceiro.domain.enums.*;
import br.com.controlefinanceiro.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
class GeradorDeParcelasServiceTest {
    private CompraRepository compras = Mockito.mock(CompraRepository.class);
    private CartaoCreditoRepository cartoes = Mockito.mock(CartaoCreditoRepository.class);
    private FaturaRepository faturas = Mockito.mock(FaturaRepository.class);
    private ParcelaRepository parcelas = Mockito.mock(ParcelaRepository.class);
    private GeradorDeParcelasService service;
    @BeforeEach void preparar() {
        service = new GeradorDeParcelasService(compras, cartoes, faturas, parcelas);
        when(compras.save(any())).thenAnswer(i -> i.getArgument(0));
        when(parcelas.saveAll(any())).thenAnswer(i -> i.getArgument(0));
    }
    @Test void geraCreditoAVistaNaFaturaDoMesSeguinteAposFechamento() {
        Compra compra = compra(TipoFormaPagamento.CREDITO, new BigDecimal("100.00"), 1, LocalDate.of(2026, 8, 26));
        CartaoCredito cartao = CartaoCredito.builder().id(9L).diaFechamento(25).diaVencimento(5).build();
        Fatura fatura = Fatura.builder().id(4L).dataVencimento(LocalDate.of(2026, 9, 5)).build();
        when(cartoes.findByFormaPagamentoId(2L)).thenReturn(Optional.of(cartao));
        when(faturas.findByCartaoCreditoIdAndMesReferencia(9L, java.time.YearMonth.of(2026, 9))).thenReturn(Optional.of(fatura));
        Compra criada = service.criar(compra);
        Parcela parcela = criada.getParcelas().getFirst();
        assertThat(parcela.getFatura()).isSameAs(fatura);
        assertThat(parcela.getDataVencimento()).isEqualTo(LocalDate.of(2026, 9, 5));
        assertThat(parcela.getStatus()).isEqualTo(StatusParcela.PENDENTE);
    }
    @Test void divideCreditoParceladoEAjustaCentavosNaUltimaParcela() {
        Compra compra = compra(TipoFormaPagamento.CREDITO, new BigDecimal("100.00"), 3, LocalDate.of(2026, 8, 10));
        CartaoCredito cartao = CartaoCredito.builder().id(9L).diaFechamento(25).diaVencimento(5).build();
        Fatura fatura = Fatura.builder().id(4L).dataVencimento(LocalDate.of(2026, 9, 5)).build();
        when(cartoes.findByFormaPagamentoId(2L)).thenReturn(Optional.of(cartao));
        when(faturas.findByCartaoCreditoIdAndMesReferencia(any(), any())).thenReturn(Optional.of(fatura));
        Compra criada = service.criar(compra);
        assertThat(criada.getParcelas()).extracting(Parcela::getValorParcela).containsExactly(new BigDecimal("33.33"), new BigDecimal("33.33"), new BigDecimal("33.34"));
        assertThat(criada.getParcelas()).allMatch(p -> p.getStatus() == StatusParcela.PENDENTE);
    }
    @Test void geraBoletoParceladoComVencimentosDeTrintaDias() {
        Compra criada = service.criar(compra(TipoFormaPagamento.BOLETO, new BigDecimal("90.00"), 3, LocalDate.of(2026, 1, 1)));
        assertThat(criada.getParcelas()).extracting(Parcela::getDataVencimento).containsExactly(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 3, 2), LocalDate.of(2026, 4, 1));
        assertThat(criada.getParcelas()).allMatch(p -> p.getFatura() == null && p.getStatus() == StatusParcela.PENDENTE);
    }
    @ParameterizedTest @EnumSource(value = TipoFormaPagamento.class, names = {
        "DINHEIRO", "DEBITO", "PIX"
    })     void marcaPagamentoImediatoComoPago(TipoFormaPagamento tipo) {
        LocalDate data = LocalDate.of(2026, 1, 10);
        Compra criada = service.criar(compra(tipo, new BigDecimal("20.00"), 1, data));
        Parcela parcela = criada.getParcelas().getFirst();
        assertThat(parcela.getStatus()).isEqualTo(StatusParcela.PAGA);
        assertThat(parcela.getDataVencimento()).isEqualTo(data);
    }
    private Compra compra(TipoFormaPagamento tipo, BigDecimal valor, int numeroParcelas, LocalDate data) {
        FormaPagamento forma = FormaPagamento.builder().id(2L).tipo(tipo).descricao("teste").build();
        return Compra.builder().formaPagamento(forma).valorTotal(valor).numParcelas(numeroParcelas).dataCompra(data).descricao("Compra").build();
    }
}
