package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByCpfCnpj(String cpfCnpj);
    boolean existsByCpfCnpj(String cpfCnpj);
}
