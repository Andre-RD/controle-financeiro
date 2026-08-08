package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findByFaturaId(Long faturaId);

    List<Parcela> findByCompraId(Long compraId);

    List<Parcela> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);

    @Query("select coalesce(sum(p.valorParcela), 0) from Parcela p where p.fatura.id = :faturaId")
    BigDecimal totalPorFaturaId(@Param("faturaId") Long faturaId);
}
