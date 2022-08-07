package br.com.felipesantos.mscliente.dto;


import br.com.felipesantos.mscliente.domain.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequest {

    private String nome;
    private String cpf;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(nome, cpf, idade);
    }
}
