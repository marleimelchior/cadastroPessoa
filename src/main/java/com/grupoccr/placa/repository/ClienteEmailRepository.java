package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.ClienteEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteEmailRepository extends JpaRepository<ClienteEmail, Long> {
}
