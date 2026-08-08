package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(@NotBlank @Size(max = 255) String nome, @NotNull TipoCategoria tipo) {
}
