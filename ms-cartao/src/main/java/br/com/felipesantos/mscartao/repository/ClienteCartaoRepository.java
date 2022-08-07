package br.com.felipesantos.mscartao.repository;

import br.com.felipesantos.mscartao.domain.ClienteCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteCartaoRepository extends JpaRepository<ClienteCartao, Long> {

    List<ClienteCartao> findByCpf(String cpf);
}
