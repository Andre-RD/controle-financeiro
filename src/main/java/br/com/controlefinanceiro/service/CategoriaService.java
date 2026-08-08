package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.CategoriaRequest;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.Categoria;
import br.com.controlefinanceiro.repository.CategoriaRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }

    @Transactional
    public Categoria criar(CategoriaRequest r) {
        return repo.save(Categoria.builder().nome(r.nome()).tipo(r.tipo()).build());
    }

    @Transactional
    public Categoria atualizar(Long id, CategoriaRequest r) {
        Categoria e = buscarPorId(id);
        e.setNome(r.nome());
        e.setTipo(r.tipo());
        return repo.save(e);
    }

    @Transactional
    public void excluir(Long id) {
        repo.delete(buscarPorId(id));
    }
}
