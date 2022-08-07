package br.com.felipesantos.msavaliadorcredito.domain;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SituacaoCliente {

    private DadoCliente dadosCliente;
    private List<CartaoCliente> cartoes;
}
