package br.com.felipesantos.mscliente.service;

import br.com.felipesantos.mscliente.domain.Cliente;
import br.com.felipesantos.mscliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
