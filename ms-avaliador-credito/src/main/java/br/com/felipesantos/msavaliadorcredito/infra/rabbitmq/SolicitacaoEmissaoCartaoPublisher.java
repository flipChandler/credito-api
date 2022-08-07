package br.com.felipesantos.msavaliadorcredito.infra.rabbitmq;

import br.com.felipesantos.msavaliadorcredito.domain.DadoEmissaoCartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartao;

    public void solicitarCartao(DadoEmissaoCartao dadoEmissaoCartao) throws JsonProcessingException {
        String json = toJson(dadoEmissaoCartao);
        rabbitTemplate.convertAndSend(queueEmissaoCartao.getName(), json);
    }

    private String toJson(DadoEmissaoCartao dadoEmissaoCartao) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dadoEmissaoCartao);

        return json;
    }
}
