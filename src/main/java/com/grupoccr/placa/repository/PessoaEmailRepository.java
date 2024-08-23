package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.PessoaEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaEmailRepository extends JpaRepository<PessoaEmail, Long> {
}
