package br.com.felipesantos.msavaliadorcredito.service;

import br.com.felipesantos.msavaliadorcredito.client.CartaoResourceClient;
import br.com.felipesantos.msavaliadorcredito.client.ClienteResourceClient;
import br.com.felipesantos.msavaliadorcredito.domain.*;
import br.com.felipesantos.msavaliadorcredito.exception.DadoClienteNotFoundException;
import br.com.felipesantos.msavaliadorcredito.exception.ErroSolicitacaoCartaoException;
import br.com.felipesantos.msavaliadorcredito.exception.ErrorComunicacaoMsException;
import br.com.felipesantos.msavaliadorcredito.infra.rabbitmq.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.math.BigDecimal.TEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartaoResourceClient cartaoResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadoClienteNotFoundException, ErrorComunicacaoMsException {
        try {
            ResponseEntity<DadoCliente> dadosClienteResponse = clienteResourceClient.findByCpf(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesClienteResponse = cartaoResourceClient.getCartoesByCpf(cpf);

            return SituacaoCliente.builder()
                    .dadosCliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesClienteResponse.getBody())
                    .build();
        } catch(FeignException.FeignClientException e) {
            int status = e.status();
            if (NOT_FOUND.value() == status) {
                throw new DadoClienteNotFoundException();
            }
            throw new ErrorComunicacaoMsException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadoClienteNotFoundException, ErrorComunicacaoMsException {
        try {
            ResponseEntity<DadoCliente> dadoClienteResponse = clienteResourceClient.findByCpf(cpf);
            ResponseEntity<List<Cartao>> cartoesClienteResponse = cartaoResourceClient.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesClienteResponse.getBody();

            List<CartaoAprovado> cartoesAprovados = cartoes.stream()
                    .map(cartao -> {
                        DadoCliente dadoCliente = dadoClienteResponse.getBody();

                        BigDecimal limiteAprovado = getLimiteAprovado(cartao, dadoCliente);

                        CartaoAprovado cartaoAprovado = getCartaoAprovadoComLimite(cartao, limiteAprovado);

                        return cartaoAprovado;
                    }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(cartoesAprovados);

        } catch(FeignException.FeignClientException e) {
            int status = e.status();
            if (NOT_FOUND.value() == status) {
                throw new DadoClienteNotFoundException();
            }
            throw new ErrorComunicacaoMsException(e.getMessage(), status);
        }
    }

    private CartaoAprovado getCartaoAprovadoComLimite(Cartao cartao, BigDecimal limiteAprovado) {
        CartaoAprovado cartaoAprovado = new CartaoAprovado();
        cartaoAprovado.setCartao(cartao.getNome());
        cartaoAprovado.setBandeira(cartao.getBandeira());
        cartaoAprovado.setLimiteAprovado(limiteAprovado);

        return cartaoAprovado;
    }

    private BigDecimal getLimiteAprovado(Cartao cartao, DadoCliente dadoCliente) {
        BigDecimal idadeBigDecimal = BigDecimal.valueOf(dadoCliente.getIdade());
        BigDecimal fator = idadeBigDecimal.divide(TEN);
        BigDecimal limiteAprovado = fator.multiply(cartao.getLimiteBasico());

        return limiteAprovado;
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadoEmissaoCartao dadoEmissaoCartao) {
        try {
            emissaoCartaoPublisher.solicitarCartao(dadoEmissaoCartao);
            String protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
