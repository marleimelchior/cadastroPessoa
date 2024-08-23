package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.PessoaTelefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaTelefoneRepository extends JpaRepository<PessoaTelefone, Long> {
}
