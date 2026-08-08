package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

    Optional<CartaoCredito> findByFormaPagamentoId(Long formaPagamentoId);
}
