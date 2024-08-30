package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.ClienteEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteEnderecoRepository extends JpaRepository<ClienteEndereco, Long> {
}
