package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.Placa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacaRepository extends JpaRepository<Placa, Long> {
//    Optional<Placa> findByCpfCnpj(String cpfCnpj);
//    boolean existsByCpfCnpj(String cpfCnpj);
    Optional<Placa> findByPlacaAndCpfCnpj(String placa, String cpfCnpj);
    boolean existsByPlaca(String placa);
}
