package br.com.controlefinanceiro.config;

import br.com.controlefinanceiro.domain.entity.*;
import br.com.controlefinanceiro.domain.enums.*;
import br.com.controlefinanceiro.repository.*;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DadosIniciaisConfig {
    @Bean
    CommandLineRunner dadosIniciais(UsuarioRepository usuarios, CategoriaRepository categorias, FormaPagamentoRepository formas, CartaoCreditoRepository cartoes) {
        return args -> {
            if (usuarios.count() > 0) return;
            Usuario usuario = usuarios.save(Usuario.builder().nome("Usuário de teste").build());
            categorias.save(Categoria.builder().nome("Alimentação").tipo(TipoCategoria.DESPESA).build());
            categorias.save(Categoria.builder().nome("Moradia").tipo(TipoCategoria.DESPESA).build());
            categorias.save(Categoria.builder().nome("Salário").tipo(TipoCategoria.RECEITA).build());
            FormaPagamento dinheiro = formas.save(FormaPagamento.builder().usuario(usuario).descricao("Dinheiro").tipo(TipoFormaPagamento.DINHEIRO).build());
            formas.save(FormaPagamento.builder().usuario(usuario).descricao("PIX").tipo(TipoFormaPagamento.PIX).build());
            formas.save(FormaPagamento.builder().usuario(usuario).descricao("Boleto").tipo(TipoFormaPagamento.BOLETO).build());
            FormaPagamento credito = formas.save(FormaPagamento.builder().usuario(usuario).descricao("Cartão principal").tipo(TipoFormaPagamento.CREDITO).build());
            cartoes.save(CartaoCredito.builder().formaPagamento(credito).diaFechamento(25).diaVencimento(5).limite(new BigDecimal("3000.00")).build());
        };
    }
}
