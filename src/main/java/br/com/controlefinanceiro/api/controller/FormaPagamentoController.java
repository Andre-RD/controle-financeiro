package br.com.controlefinanceiro.api.controller;

import br.com.controlefinanceiro.api.ApiMapper;
import br.com.controlefinanceiro.api.dto.*;
import br.com.controlefinanceiro.service.FormaPagamentoService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoService service;

    public FormaPagamentoController(FormaPagamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<FormaPagamentoResponse> listar(@RequestParam(required = false) Long usuarioId) {
        return service.listar(usuarioId).stream().map(ApiMapper::forma).toList();
    }

    @GetMapping("/{id}")
    public FormaPagamentoResponse buscar(@PathVariable Long id) {
        return ApiMapper.forma(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FormaPagamentoResponse> criar(@Valid @RequestBody FormaPagamentoRequest r) {
        var e = service.criar(r);
        return ResponseEntity.created(URI.create("/formas-pagamento/" + e.getId())).body(ApiMapper.forma(e));
    }

    @PutMapping("/{id}")
    public FormaPagamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody FormaPagamentoRequest r) {
        return ApiMapper.forma(service.atualizar(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
