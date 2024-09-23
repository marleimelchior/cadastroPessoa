package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.PlacaConcessionaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacaConcessionariaRepository extends JpaRepository<PlacaConcessionaria, Long> {
}
