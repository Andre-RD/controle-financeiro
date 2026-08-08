package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.api.dto.CompraRequest;
import br.com.controlefinanceiro.api.exception.RecursoNaoEncontradoException;
import br.com.controlefinanceiro.api.exception.RegraDeNegocioException;
import br.com.controlefinanceiro.domain.entity.Compra;
import br.com.controlefinanceiro.domain.entity.Parcela;
import br.com.controlefinanceiro.domain.enums.StatusCompra;
import br.com.controlefinanceiro.domain.enums.StatusParcela;
import br.com.controlefinanceiro.repository.CompraRepository;
import br.com.controlefinanceiro.repository.ParcelaRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CompraService {

    private final CompraRepository compras;
    private final ParcelaRepository parcelas;
    private final UsuarioService usuarios;
    private final CategoriaService categorias;
    private final FormaPagamentoService formas;
    private final GastoFixoService gastosFixos;
    private final GeradorDeParcelasService gerador;

    public CompraService(
            CompraRepository compras,
            ParcelaRepository parcelas,
            UsuarioService usuarios,
            CategoriaService categorias,
            FormaPagamentoService formas,
            GastoFixoService gastosFixos,
            GeradorDeParcelasService gerador) {
        this.compras = compras;
        this.parcelas = parcelas;
        this.usuarios = usuarios;
        this.categorias = categorias;
        this.formas = formas;
        this.gastosFixos = gastosFixos;
        this.gerador = gerador;
    }

    @Transactional
    public Compra criar(CompraRequest r) {
        Compra compra = new Compra();
        aplicar(compra, r);
        return gerador.criar(compra);
    }

    public List<Compra> listar(Long usuarioId, Long categoriaId, LocalDate inicio, LocalDate fim) {
        LocalDate de = inicio == null ? LocalDate.of(1900, 1, 1) : inicio;
        LocalDate ate = fim == null ? LocalDate.of(9999, 12, 31) : fim;
        if (usuarioId != null && categoriaId != null) {
            return compras.findByUsuarioIdAndCategoriaIdAndDataCompraBetweenOrderByDataCompraDesc(
                    usuarioId, categoriaId, de, ate);
        }
        if (usuarioId != null) {
            return compras.findByUsuarioIdAndDataCompraBetweenOrderByDataCompraDesc(usuarioId, de, ate);
        }
        if (categoriaId != null) {
            return compras.findByCategoriaIdAndDataCompraBetweenOrderByDataCompraDesc(categoriaId, de, ate);
        }
        return compras.findByDataCompraBetweenOrderByDataCompraDesc(de, ate);
    }

    public Compra buscarPorId(Long id) {
        return compras.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Compra", id));
    }

    public List<Parcela> parcelasPorCompraId(Long compraId) {
        return parcelas.findByCompraId(compraId);
    }

    /**
     * Compras são imutáveis após a criação porque suas parcelas podem já ter sido pagas.
     * O cancelamento preserva o histórico e apenas altera os status da compra e das parcelas.
     */
    @Transactional
    public Compra cancelar(Long compraId, boolean confirmarParcelasPagas) {
        Compra compra = buscarPorId(compraId);
        List<Parcela> parcelasDaCompra = parcelas.findByCompraId(compraId);

        boolean possuiParcelaPaga = parcelasDaCompra.stream()
                .anyMatch(parcela -> parcela.getStatus() == StatusParcela.PAGA);
        if (possuiParcelaPaga && !confirmarParcelasPagas) {
            throw new RegraDeNegocioException(
                    "A compra possui parcelas pagas. Informe confirmarParcelasPagas=true para cancelá-la.");
        }

        if (compra.getStatus() != StatusCompra.CANCELADA) {
            compra.setStatus(StatusCompra.CANCELADA);
            parcelasDaCompra.forEach(parcela -> parcela.setStatus(StatusParcela.CANCELADA));
        }
        return compra;
    }

    private void aplicar(Compra e, CompraRequest r) {
        e.setUsuario(usuarios.buscarPorId(r.usuarioId()));
        e.setCategoria(categorias.buscarPorId(r.categoriaId()));
        e.setFormaPagamento(formas.buscarPorId(r.formaPagamentoId()));
        e.setGastoFixo(r.gastoFixoId() == null ? null : gastosFixos.buscarPorId(r.gastoFixoId()));
        e.setDescricao(r.descricao());
        e.setValorTotal(r.valorTotal());
        e.setDataCompra(r.dataCompra());
        e.setNumParcelas(r.numParcelas());
    }
}
