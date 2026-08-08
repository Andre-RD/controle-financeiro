package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.domain.entity.Categoria;
import br.com.controlefinanceiro.domain.enums.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByTipo(TipoCategoria tipo);
}
