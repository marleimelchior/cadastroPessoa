package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.Placa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacaRepository extends JpaRepository<Placa, Long> {

    Optional<Placa> findByPlacaAndCliente_CpfCnpj(String placa, String cpfCnpj);
    boolean existsByPlaca(String placa);

    List<Placa> findAll();
}
