package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.Fatura;
import br.com.controlefinanceiro.domain.entity.Parcela;
import br.com.controlefinanceiro.repository.FaturaRepository;
import br.com.controlefinanceiro.repository.ParcelaRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FaturaService {

    private final FaturaRepository faturas;
    private final ParcelaRepository parcelas;

    public FaturaService(FaturaRepository faturas, ParcelaRepository parcelas) {
        this.faturas = faturas;
        this.parcelas = parcelas;
    }

    public Fatura buscarPorId(Long id) {
        return faturas.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Fatura", id));
    }

    public Fatura buscarPorCartaoEMes(Long cartaoId, YearMonth mesReferencia) {
        return faturas.findByCartaoCreditoIdAndMesReferencia(cartaoId, mesReferencia)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fatura para cartão/mês", cartaoId));
    }

    public BigDecimal totalPorFaturaId(Long faturaId) {
        return parcelas.totalPorFaturaId(faturaId);
    }

    public List<Parcela> parcelasPorFaturaId(Long faturaId) {
        return parcelas.findByFaturaId(faturaId);
    }
}
