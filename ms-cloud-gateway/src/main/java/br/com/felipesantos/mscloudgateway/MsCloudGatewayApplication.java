package br.com.felipesantos.mscloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class MsCloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCloudGatewayApplication.class, args);
	}

	// lb :: load balancer
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(rota -> rota.path("/api/clientes/**")
						.uri("lb://ms-cliente"))
				.route(rota -> rota.path("/api/cartoes/**")
						.uri("lb://ms-cartao"))
				.route(rota -> rota.path("/api/avaliacoes-credito/**")
						.uri("lb://ms-avaliador-credito"))
				.build();
	}
}
