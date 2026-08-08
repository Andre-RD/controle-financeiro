package br.com.controlefinanceiro.domain.entity;

import br.com.controlefinanceiro.domain.enums.Recorrencia;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Gasto fixo ou recorrente (aluguel, contas, assinaturas).
 * <p>
 * Um job futuro gerará {@link Compra} a partir de registros com {@link #ativo} {@code true}.
 * Desativar ({@code ativo = false}) impede novas compras automáticas, sem apagar histórico.
 * {@link #valorPrevisto} é referência; cada {@link Compra} gerada pode ter {@code valorTotal} diferente.
 */
@Entity
@Table(name = "gasto_fixo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoFixo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "forma_pagamento_id", nullable = false)
    private FormaPagamento formaPagamento;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descricao;

    @NotNull
    @Positive
    @Column(name = "valor_previsto", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPrevisto;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Recorrencia recorrencia;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    /**
     * Indica se o gasto fixo está encerrado na data de referência (ex.: contrato com {@link #dataFim} no passado).
     */
    public boolean isEncerradoEm(LocalDate referencia) {
        return dataFim != null && referencia.isAfter(dataFim);
    }

    @AssertTrue(message = "dataFim não pode ser anterior a dataInicio")
    public boolean isPeriodoValido() {
        if (dataFim == null) {
            return true;
        }
        return !dataFim.isBefore(dataInicio);
    }
}
