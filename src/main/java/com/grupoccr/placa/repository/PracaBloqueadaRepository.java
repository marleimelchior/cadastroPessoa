package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.PracaBloqueada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracaBloqueadaRepository extends JpaRepository<PracaBloqueada, PracaBloqueada.PracaBloqueadaId> {
}
