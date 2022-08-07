package br.com.felipesantos.msavaliadorcredito.controller;

import br.com.felipesantos.msavaliadorcredito.domain.*;
import br.com.felipesantos.msavaliadorcredito.exception.DadoClienteNotFoundException;
import br.com.felipesantos.msavaliadorcredito.exception.ErroSolicitacaoCartaoException;
import br.com.felipesantos.msavaliadorcredito.exception.ErrorComunicacaoMsException;
import br.com.felipesantos.msavaliadorcredito.service.AvaliadorCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam String cpf) {
        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadoClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorComunicacaoMsException e) {
            return ResponseEntity
                    .status(HttpStatus.resolve(e.getStatus()))
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadoAvaliacao dadoAvaliacao) {
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente =
                    avaliadorCreditoService.realizarAvaliacao(dadoAvaliacao.getCpf(), dadoAvaliacao.getRenda());

            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadoClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErrorComunicacaoMsException e) {
            return ResponseEntity
                    .status(HttpStatus.resolve(e.getStatus()))
                    .body(e.getMessage());
        }
    }

    @PostMapping("solicitacao-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadoEmissaoCartao dadoEmissaoCartao) {
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService
                    .solicitarEmissaoCartao(dadoEmissaoCartao);

            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        } catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}
