package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.ClienteTelefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteTelefoneRepository extends JpaRepository<ClienteTelefone, Long> {
}
