package br.com.controlefinanceiro.api.dto;
import br.com.controlefinanceiro.domain.enums.StatusParcela;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ParcelaResponse(Long id, Integer numeroParcela, BigDecimal valorParcela, Long faturaId, LocalDate dataVencimento, StatusParcela status) {
}
