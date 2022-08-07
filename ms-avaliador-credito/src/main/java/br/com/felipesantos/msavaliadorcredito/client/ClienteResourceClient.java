package br.com.felipesantos.msavaliadorcredito.client;

import br.com.felipesantos.msavaliadorcredito.domain.DadoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ms-cliente", path = "/api/clientes")
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadoCliente> findByCpf(@RequestParam String cpf);
}
