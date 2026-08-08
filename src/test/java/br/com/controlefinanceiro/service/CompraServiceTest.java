package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.exception.RegraDeNegocioException;
import br.com.controlefinanceiro.domain.entity.Compra;
import br.com.controlefinanceiro.domain.entity.Parcela;
import br.com.controlefinanceiro.domain.enums.StatusCompra;
import br.com.controlefinanceiro.domain.enums.StatusParcela;
import br.com.controlefinanceiro.repository.CompraRepository;
import br.com.controlefinanceiro.repository.ParcelaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompraServiceTest {

    @Test
    void cancelaCompraEParcelasPendentes() {
        Compra compra = compra(10L);
        Parcela parcela = parcela(StatusParcela.PENDENTE);
        CompraService service = servicePara(compra, List.of(parcela));

        service.cancelar(10L, false);

        assertThat(compra.getStatus()).isEqualTo(StatusCompra.CANCELADA);
        assertThat(parcela.getStatus()).isEqualTo(StatusParcela.CANCELADA);
    }

    @Test
    void exigeConfirmacaoQuandoHaParcelaPaga() {
        Compra compra = compra(10L);
        Parcela parcela = parcela(StatusParcela.PAGA);
        CompraService service = servicePara(compra, List.of(parcela));

        assertThatThrownBy(() -> service.cancelar(10L, false))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("confirmarParcelasPagas=true");

        assertThat(compra.getStatus()).isEqualTo(StatusCompra.ATIVA);
        assertThat(parcela.getStatus()).isEqualTo(StatusParcela.PAGA);
    }

    @Test
    void cancelaCompraComParcelaPagaQuandoConfirmado() {
        Compra compra = compra(10L);
        Parcela parcela = parcela(StatusParcela.PAGA);
        CompraService service = servicePara(compra, List.of(parcela));

        service.cancelar(10L, true);

        assertThat(compra.getStatus()).isEqualTo(StatusCompra.CANCELADA);
        assertThat(parcela.getStatus()).isEqualTo(StatusParcela.CANCELADA);
    }

    private CompraService servicePara(Compra compra, List<Parcela> parcelasDaCompra) {
        CompraRepository compras = mock(CompraRepository.class);
        ParcelaRepository parcelas = mock(ParcelaRepository.class);
        when(compras.findById(compra.getId())).thenReturn(Optional.of(compra));
        when(parcelas.findByCompraId(compra.getId())).thenReturn(parcelasDaCompra);
        return new CompraService(
                compras,
                parcelas,
                mock(UsuarioService.class),
                mock(CategoriaService.class),
                mock(FormaPagamentoService.class),
                mock(GastoFixoService.class),
                mock(GeradorDeParcelasService.class));
    }

    private Compra compra(Long id) {
        Compra compra = new Compra();
        compra.setId(id);
        compra.setStatus(StatusCompra.ATIVA);
        return compra;
    }

    private Parcela parcela(StatusParcela status) {
        Parcela parcela = new Parcela();
        parcela.setStatus(status);
        return parcela;
    }
}
