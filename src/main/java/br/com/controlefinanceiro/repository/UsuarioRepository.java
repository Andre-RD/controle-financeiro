package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
