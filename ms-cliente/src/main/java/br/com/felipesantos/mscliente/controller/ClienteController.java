package br.com.felipesantos.mscliente.controller;

import br.com.felipesantos.mscliente.domain.Cliente;
import br.com.felipesantos.mscliente.dto.ClienteRequest;
import br.com.felipesantos.mscliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping(params = "cpf")
    public ResponseEntity findByCpf(@RequestParam String cpf) {
        var optionalCliente = clienteService.findByCpf(cpf);
        if (optionalCliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalCliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        log.info("status do ms cliente");
        return ResponseEntity.ok(clienteService.findAll());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteRequest clienteRequest) {
        var cliente = clienteRequest.toModel();
        clienteService.save(cliente);

        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }
}
