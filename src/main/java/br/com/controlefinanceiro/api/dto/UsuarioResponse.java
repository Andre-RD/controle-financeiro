package br.com.controlefinanceiro.api.dto;

import java.time.Instant;

public record UsuarioResponse(Long id, String nome, Instant createdAt) {
}
