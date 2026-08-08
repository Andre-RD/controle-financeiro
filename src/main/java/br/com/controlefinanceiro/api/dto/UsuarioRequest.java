package br.com.controlefinanceiro.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(@NotBlank @Size(max = 255) String nome) {
}
