package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.FormaPagamentoRequest;
import br.com.controlefinanceiro.api.exception.RegraDeNegocioException;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.FormaPagamento;
import br.com.controlefinanceiro.domain.enums.TipoFormaPagamento;
import br.com.controlefinanceiro.repository.FormaPagamentoRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FormaPagamentoService {

    private final FormaPagamentoRepository repo;
    private final UsuarioService usuarios;

    public FormaPagamentoService(FormaPagamentoRepository repo, UsuarioService usuarios) {
        this.repo = repo;
        this.usuarios = usuarios;
    }

    public List<FormaPagamento> listar(Long usuarioId) {
        return usuarioId == null ? repo.findAll() : repo.findByUsuarioId(usuarioId);
    }

    public FormaPagamento buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Forma de pagamento", id));
    }

    @Transactional
    public FormaPagamento criar(FormaPagamentoRequest r) {
        FormaPagamento e = new FormaPagamento();
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public FormaPagamento atualizar(Long id, FormaPagamentoRequest r) {
        FormaPagamento e = buscarPorId(id);
        if (e.getCartao() != null && r.tipo() != TipoFormaPagamento.CREDITO) {
            throw new RegraDeNegocioException("Uma forma com cartão deve permanecer do tipo CREDITO");
        }
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public void excluir(Long id) {
        repo.delete(buscarPorId(id));
    }

    private void aplicar(FormaPagamento e, FormaPagamentoRequest r) {
        e.setUsuario(usuarios.buscarPorId(r.usuarioId()));
        e.setDescricao(r.descricao());
        e.setTipo(r.tipo());
    }
}
