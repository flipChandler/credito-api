package br.com.felipesantos.mscartao.service;

import br.com.felipesantos.mscartao.domain.ClienteCartao;
import br.com.felipesantos.mscartao.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> getCartoesByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }
}
