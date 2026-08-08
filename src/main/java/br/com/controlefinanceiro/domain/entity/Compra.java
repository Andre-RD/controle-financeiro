package br.com.controlefinanceiro.domain.entity;

import br.com.controlefinanceiro.domain.enums.StatusCompra;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Evento de gasto. Compras originadas de {@link GastoFixo} têm {@link #gastoFixo} preenchido e
 * {@link #numParcelas} sempre 1; parcelas e fatura seguem as mesmas regras das compras avulsas.
 */
@Entity
@Table(name = "compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasto_fixo_id")
    private GastoFixo gastoFixo;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descricao;

    @NotNull
    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @NotNull
    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @NotNull
    @Positive
    @Column(name = "num_parcelas", nullable = false)
    @Builder.Default
    private Integer numParcelas = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusCompra status = StatusCompra.ATIVA;

    @OneToMany(mappedBy = "compra", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Parcela> parcelas = new ArrayList<>();

    @AssertTrue(message = "compra originada de gasto fixo deve ter numParcelas = 1")
    public boolean isNumParcelasValidoParaGastoFixo() {
        if (gastoFixo == null) {
            return true;
        }
        return numParcelas != null && numParcelas == 1;
    }
}
