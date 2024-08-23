package com.grupoccr.placa.repository;

import com.grupoccr.placa.model.entity.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerRepository extends JpaRepository<Logger, Long> {
}
