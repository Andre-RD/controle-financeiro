package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.repository.FaturaRepository;
import br.com.controlefinanceiro.repository.ParcelaRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FaturaServiceTest {

    @Test
    void calculaTotalNoMomentoDaConsulta() {
        ParcelaRepository parcelas = mock(ParcelaRepository.class);
        FaturaService service = new FaturaService(mock(FaturaRepository.class), parcelas);
        when(parcelas.totalPorFaturaId(42L)).thenReturn(new BigDecimal("149.90"));

        BigDecimal total = service.totalPorFaturaId(42L);

        assertThat(total).isEqualByComparingTo("149.90");
        verify(parcelas).totalPorFaturaId(42L);
    }
}
