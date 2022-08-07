package br.com.felipesantos.mscartao.infra.rabbitmq;

import br.com.felipesantos.mscartao.domain.Cartao;
import br.com.felipesantos.mscartao.domain.ClienteCartao;
import br.com.felipesantos.mscartao.domain.DadoEmissaoCartao;
import br.com.felipesantos.mscartao.repository.CartaoRepository;
import br.com.felipesantos.mscartao.repository.ClienteCartaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartao}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DadoEmissaoCartao dadoEmissaoCartao = mapper.readValue(payload, DadoEmissaoCartao.class);

            Cartao cartao = cartaoRepository.findById(dadoEmissaoCartao.getIdCartao())
                    .orElseThrow();

            ClienteCartao clienteCartao = getClienteCartao(dadoEmissaoCartao, cartao);

            clienteCartaoRepository.save(clienteCartao);
        } catch (Exception e) {
            log.error("Erro ao receber solicitação de emissão de cartão: {}", e.getMessage());
        }
    }

    private ClienteCartao getClienteCartao(DadoEmissaoCartao dadoEmissaoCartao, Cartao cartao) {
        ClienteCartao clienteCartao = new ClienteCartao();
        clienteCartao.setCartao(cartao);
        clienteCartao.setCpf(dadoEmissaoCartao.getCpf());
        clienteCartao.setLimite(dadoEmissaoCartao.getLimiteLiberado());

        return clienteCartao;
    }
}
