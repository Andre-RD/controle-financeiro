package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.GastoFixo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoFixoRepository extends JpaRepository<GastoFixo, Long> {

    List<GastoFixo> findByUsuarioIdAndAtivoTrue(Long usuarioId);
}
