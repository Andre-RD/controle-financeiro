package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByUsuarioIdOrderByDataCompraDesc(Long usuarioId);

    List<Compra> findByGastoFixoIdAndDataCompraBetween(
            Long gastoFixoId, LocalDate inicio, LocalDate fim);

    List<Compra> findByUsuarioIdAndCategoriaIdAndDataCompraBetweenOrderByDataCompraDesc(
            Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fim);

    List<Compra> findByUsuarioIdAndDataCompraBetweenOrderByDataCompraDesc(
            Long usuarioId, LocalDate inicio, LocalDate fim);

    List<Compra> findByCategoriaIdAndDataCompraBetweenOrderByDataCompraDesc(
            Long categoriaId, LocalDate inicio, LocalDate fim);

    List<Compra> findByDataCompraBetweenOrderByDataCompraDesc(LocalDate inicio, LocalDate fim);

    Optional<Compra> findById(Long id);
}
