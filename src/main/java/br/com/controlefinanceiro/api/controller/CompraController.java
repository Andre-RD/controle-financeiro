package br.com.controlefinanceiro.api.controller;

import br.com.controlefinanceiro.api.ApiMapper;
import br.com.controlefinanceiro.api.dto.CompraRequest;
import br.com.controlefinanceiro.api.dto.CompraResponse;
import br.com.controlefinanceiro.domain.entity.Compra;
import br.com.controlefinanceiro.service.CompraService;
import jakarta.validation.Valid;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompraResponse> criar(@Valid @RequestBody CompraRequest r) {
        Compra compra = service.criar(r);
        return ResponseEntity.created(URI.create("/compras/" + compra.getId())).body(resposta(compra));
    }

    @GetMapping
    public List<CompraResponse> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim) {
        return service.listar(usuarioId, categoriaId, inicio, fim).stream().map(this::resposta).toList();
    }

    @GetMapping("/{id}")
    public CompraResponse buscar(@PathVariable Long id) {
        return resposta(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public CompraResponse cancelar(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirmarParcelasPagas) {
        return resposta(service.cancelar(id, confirmarParcelasPagas));
    }

    private CompraResponse resposta(Compra e) {
        return ApiMapper.compra(e, service.parcelasPorCompraId(e.getId()));
    }
}
