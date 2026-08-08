package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.UsuarioRequest;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.Usuario;
import br.com.controlefinanceiro.repository.UsuarioRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    @Transactional
    public Usuario criar(UsuarioRequest r) {
        return repo.save(Usuario.builder().nome(r.nome()).build());
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequest r) {
        Usuario e = buscarPorId(id);
        e.setNome(r.nome());
        return repo.save(e);
    }

    @Transactional
    public void excluir(Long id) {
        repo.delete(buscarPorId(id));
    }
}
