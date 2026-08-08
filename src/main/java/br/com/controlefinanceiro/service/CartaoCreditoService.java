package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.CartaoCreditoRequest;
import br.com.controlefinanceiro.api.exception.RegraDeNegocioException;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.domain.entity.CartaoCredito;
import br.com.controlefinanceiro.domain.entity.FormaPagamento;
import br.com.controlefinanceiro.domain.enums.TipoFormaPagamento;
import br.com.controlefinanceiro.repository.CartaoCreditoRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartaoCreditoService {

    private final CartaoCreditoRepository repo;
    private final FormaPagamentoService formas;

    public CartaoCreditoService(CartaoCreditoRepository repo, FormaPagamentoService formas) {
        this.repo = repo;
        this.formas = formas;
    }

    public List<CartaoCredito> listar() {
        return repo.findAll();
    }

    public CartaoCredito buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cartão", id));
    }

    @Transactional
    public CartaoCredito criar(CartaoCreditoRequest r) {
        if (repo.findByFormaPagamentoId(r.formaPagamentoId()).isPresent()) {
            throw new RegraDeNegocioException("A forma de pagamento já possui cartão");
        }
        CartaoCredito e = new CartaoCredito();
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public CartaoCredito atualizar(Long id, CartaoCreditoRequest r) {
        CartaoCredito e = buscarPorId(id);
        aplicar(e, r);
        return repo.save(e);
    }

    @Transactional
    public void excluir(Long id) {
        repo.delete(buscarPorId(id));
    }

    private void aplicar(CartaoCredito e, CartaoCreditoRequest r) {
        FormaPagamento forma = formas.buscarPorId(r.formaPagamentoId());
        if (forma.getTipo() != TipoFormaPagamento.CREDITO) {
            throw new RegraDeNegocioException("Cartão só pode usar forma de pagamento CREDITO");
        }
        e.setFormaPagamento(forma);
        e.setDiaFechamento(r.diaFechamento());
        e.setDiaVencimento(r.diaVencimento());
        e.setLimite(r.limite());
    }
}
