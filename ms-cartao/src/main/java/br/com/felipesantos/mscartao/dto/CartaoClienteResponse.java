package br.com.felipesantos.mscartao.dto;

import br.com.felipesantos.mscartao.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartaoClienteResponse of(ClienteCartao model) {
        return new CartaoClienteResponse(
                model.getCartao().getNome(),
                model.getCartao().getBandeira().toString(),
                model.getLimite()
        );
    }
}
