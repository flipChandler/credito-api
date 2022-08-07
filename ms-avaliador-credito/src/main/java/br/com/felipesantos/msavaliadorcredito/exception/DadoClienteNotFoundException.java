package br.com.felipesantos.msavaliadorcredito.exception;

public class DadoClienteNotFoundException extends Exception {

    public DadoClienteNotFoundException() {
        super("Dados do cliente não informado para o CPF informado.");
    }
}
