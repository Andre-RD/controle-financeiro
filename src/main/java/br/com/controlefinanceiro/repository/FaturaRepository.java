package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    @Query("SELECT f FROM Fatura f WHERE f.cartao.id = :cartaoId AND f.mesReferencia = :mesReferencia")
    Optional<Fatura> findByCartaoCreditoIdAndMesReferencia(
            @Param("cartaoId") Long cartaoId,
            @Param("mesReferencia") YearMonth mesReferencia);
}
