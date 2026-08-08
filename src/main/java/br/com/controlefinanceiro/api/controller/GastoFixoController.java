package br.com.controlefinanceiro.api.controller;

import br.com.controlefinanceiro.api.ApiMapper;
import br.com.controlefinanceiro.api.dto.*;
import br.com.controlefinanceiro.service.GastoFixoService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gastos-fixos")
public class GastoFixoController {

    private final GastoFixoService service;

    public GastoFixoController(GastoFixoService service) {
        this.service = service;
    }

    @GetMapping
    public List<GastoFixoResponse> listar(@RequestParam(required = false) Long usuarioId) {
        return service.listar(usuarioId).stream().map(ApiMapper::gasto).toList();
    }

    @GetMapping("/{id}")
    public GastoFixoResponse buscar(@PathVariable Long id) {
        return ApiMapper.gasto(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<GastoFixoResponse> criar(@Valid @RequestBody GastoFixoRequest r) {
        var e = service.criar(r);
        return ResponseEntity.created(URI.create("/gastos-fixos/" + e.getId())).body(ApiMapper.gasto(e));
    }

    @PutMapping("/{id}")
    public GastoFixoResponse atualizar(@PathVariable Long id, @Valid @RequestBody GastoFixoRequest r) {
        return ApiMapper.gasto(service.atualizar(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
