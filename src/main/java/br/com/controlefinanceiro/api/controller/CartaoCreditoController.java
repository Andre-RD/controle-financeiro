package br.com.controlefinanceiro.api.controller;

import br.com.controlefinanceiro.api.ApiMapper;
import br.com.controlefinanceiro.api.dto.*;
import br.com.controlefinanceiro.service.CartaoCreditoService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes-credito")
public class CartaoCreditoController {

    private final CartaoCreditoService service;

    public CartaoCreditoController(CartaoCreditoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CartaoCreditoResponse> listar() {
        return service.listar().stream().map(ApiMapper::cartao).toList();
    }

    @GetMapping("/{id}")
    public CartaoCreditoResponse buscar(@PathVariable Long id) {
        return ApiMapper.cartao(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CartaoCreditoResponse> criar(@Valid @RequestBody CartaoCreditoRequest r) {
        var e = service.criar(r);
        return ResponseEntity.created(URI.create("/cartoes-credito/" + e.getId())).body(ApiMapper.cartao(e));
    }

    @PutMapping("/{id}")
    public CartaoCreditoResponse atualizar(@PathVariable Long id, @Valid @RequestBody CartaoCreditoRequest r) {
        return ApiMapper.cartao(service.atualizar(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
