package br.com.felipesantos.mscartao.dto;

import br.com.felipesantos.mscartao.domain.Cartao;
import br.com.felipesantos.mscartao.enums.BandeiraCartao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartaoRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
