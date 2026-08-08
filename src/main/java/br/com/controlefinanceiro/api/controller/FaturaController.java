package br.com.controlefinanceiro.api.controller;

import br.com.controlefinanceiro.api.ApiMapper;
import br.com.controlefinanceiro.api.dto.FaturaResponse;
import br.com.controlefinanceiro.domain.entity.Fatura;
import br.com.controlefinanceiro.service.FaturaService;

import java.time.YearMonth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faturas")
public class FaturaController {

    private final FaturaService service;

    public FaturaController(FaturaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public FaturaResponse porId(@PathVariable Long id) {
        return resposta(service.buscarPorId(id));
    }

    @GetMapping
    public FaturaResponse porCartaoEMes(@RequestParam Long cartaoId, @RequestParam YearMonth mesReferencia) {
        return resposta(service.buscarPorCartaoEMes(cartaoId, mesReferencia));
    }

    private FaturaResponse resposta(Fatura f) {
        return ApiMapper.fatura(
                f,
                service.totalPorFaturaId(f.getId()),
                service.parcelasPorFaturaId(f.getId()));
    }
}
