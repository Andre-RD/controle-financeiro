package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.GastoFixoRequest;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.GastoFixo;
import br.com.controlefinanceiro.repository.GastoFixoRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GastoFixoService {

    private final GastoFixoRepository repo;
    private final UsuarioService usuarios;
    private final CategoriaService categorias;
    private final FormaPagamentoService formas;

    public GastoFixoService(
            GastoFixoRepository repo,
            UsuarioService usuarios,
            CategoriaService categorias,
            FormaPagamentoService formas) {
        this.repo = repo;
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.formas = formas;
    }

    public List<GastoFixo> listar(Long usuarioId) {
        return usuarioId == null ? repo.findAll() : repo.findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    public GastoFixo buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Gasto fixo", id));
    }

    @Transactional
    public GastoFixo criar(GastoFixoRequest r) {
        GastoFixo e = new GastoFixo();
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public GastoFixo atualizar(Long id, GastoFixoRequest r) {
        GastoFixo e = buscarPorId(id);
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public void excluir(Long id) {
        repo.delete(buscarPorId(id));
    }

    private void aplicar(GastoFixo e, GastoFixoRequest r) {
        e.setUsuario(usuarios.buscarPorId(r.usuarioId()));
        e.setCategoria(categorias.buscarPorId(r.categoriaId()));
        e.setFormaPagamento(formas.buscarPorId(r.formaPagamentoId()));
        e.setDescricao(r.descricao());
        e.setValorPrevisto(r.valorPrevisto());
        e.setDiaVencimento(r.diaVencimento());
        e.setRecorrencia(r.recorrencia());
        e.setDataInicio(r.dataInicio());
        e.setDataFim(r.dataFim());
        e.setAtivo(r.ativo());
    }
}
