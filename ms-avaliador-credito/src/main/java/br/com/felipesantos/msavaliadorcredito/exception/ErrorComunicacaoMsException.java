package br.com.felipesantos.msavaliadorcredito.exception;

import lombok.Getter;

public class ErrorComunicacaoMsException extends Exception {

    @Getter
    private Integer status;

    public ErrorComunicacaoMsException(String mensagem, Integer status) {
        super(mensagem);
        this.status = status;
    }
}
