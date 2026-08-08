package br.com.controlefinanceiro.api.dto;

import br.com.controlefinanceiro.domain.enums.TipoFormaPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FormaPagamentoRequest(@NotNull @Positive Long usuarioId, @NotBlank @Size(max = 255) String descricao,
                                    @NotNull TipoFormaPagamento tipo) {
}
