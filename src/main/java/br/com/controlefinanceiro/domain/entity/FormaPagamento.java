package br.com.controlefinanceiro.domain.entity;

import br.com.controlefinanceiro.domain.enums.TipoFormaPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Forma de pagamento do usuário.
 * <p>
 * Invariante de domínio: {@link #cartao} só pode estar preenchido quando {@link #tipo} é
 * {@link TipoFormaPagamento#CREDITO}; para os demais tipos deve ser {@code null}.
 */
@Entity
@Table(name = "forma_pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoFormaPagamento tipo;

    @OneToOne(mappedBy = "formaPagamento", fetch = FetchType.LAZY)
    private CartaoCredito cartao;

    @AssertTrue(message = "cartao deve ser null exceto quando tipo é CREDITO")
    public boolean isCartaoConsistenteComTipo() {
        return tipo == TipoFormaPagamento.CREDITO || cartao == null;
    }
}
