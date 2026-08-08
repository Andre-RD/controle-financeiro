package br.com.controlefinanceiro.domain.entity;

import br.com.controlefinanceiro.domain.converter.YearMonthAttributeConverter;
import br.com.controlefinanceiro.domain.enums.StatusFatura;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Fatura de cartão de crédito para um mês de referência.
 * <p>
 * O valor total da fatura não é persistido: deve ser sempre derivado pela soma das
 * {@link Parcela} vinculadas a esta fatura.
 */
@Entity
@Table(
        name = "fatura",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_fatura_cartao_mes_referencia",
                columnNames = {"cartao_credito_id", "mes_referencia"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cartao_credito_id", nullable = false)
    private CartaoCredito cartao;

    @NotNull
    @Convert(converter = YearMonthAttributeConverter.class)
    @Column(name = "mes_referencia", nullable = false, length = 7)
    private YearMonth mesReferencia;

    @NotNull
    @Column(name = "data_fechamento", nullable = false)
    private LocalDate dataFechamento;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusFatura status;
}
