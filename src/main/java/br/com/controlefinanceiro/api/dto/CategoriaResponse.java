package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.TipoCategoria;

public record CategoriaResponse(Long id, String nome, TipoCategoria tipo) {
}
