package br.com.felipesantos.mscartao.controller;

import br.com.felipesantos.mscartao.domain.Cartao;
import br.com.felipesantos.mscartao.domain.ClienteCartao;
import br.com.felipesantos.mscartao.dto.CartaoClienteResponse;
import br.com.felipesantos.mscartao.dto.CartaoRequest;
import br.com.felipesantos.mscartao.service.CartaoService;
import br.com.felipesantos.mscartao.service.ClienteCartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam Long renda) {
        return ResponseEntity.ok(cartaoService.getCartoesRendaMenorIgual(renda));
    }

    @GetMapping
    public ResponseEntity<List<Cartao>> getCartoes() {
        return ResponseEntity.ok(cartaoService.findAll());
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartaoClienteResponse>> getCartoesByCpf(@RequestParam String cpf) {
        List<ClienteCartao> lista = clienteCartaoService.getCartoesByCpf(cpf);
        List<CartaoClienteResponse> resultList = lista.stream()
                .map(CartaoClienteResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity save(@RequestBody CartaoRequest cartaoRequest) {
        Cartao cartao = cartaoRequest.toModel();
        cartaoService.save(cartao);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
