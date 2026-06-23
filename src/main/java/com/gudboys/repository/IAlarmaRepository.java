package com.gudboys.repository;

import com.gudboys.domain.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IAlarmaRepository extends JpaRepository<Alarma, Long> {

    List<Alarma> findByAnimalId(Long animalId);

    /** Alarmas cuyo próximo disparo ya venció (A.4). El filtro por estado ACTIVA se aplica en el service. */
    List<Alarma> findByFechaProximoDisparoBefore(LocalDateTime fecha);
}
