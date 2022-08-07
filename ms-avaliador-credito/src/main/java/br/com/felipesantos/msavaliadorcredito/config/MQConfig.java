package br.com.felipesantos.msavaliadorcredito.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.emissao-cartao}")
    private String emissaoCartaoQueue;

    @Bean
    public Queue queueEmissaoCartao() {
        return new Queue(emissaoCartaoQueue, true);
    }
}
