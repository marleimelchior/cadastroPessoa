package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.Parceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParceiroRepository extends JpaRepository<Parceiro, Long> {
    Optional<Parceiro> findById(Long id);
}
