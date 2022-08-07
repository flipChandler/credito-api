package br.com.felipesantos.msavaliadorcredito.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RetornoAvaliacaoCliente {

    private List<CartaoAprovado> cartoes;
}
