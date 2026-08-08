package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    List<FormaPagamento> findByUsuarioId(Long usuarioId);
}
