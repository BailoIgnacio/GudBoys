package com.gudboys.repository;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAlarmaRepository extends JpaRepository<Alarma, Long> {

    List<Alarma> findByAnimalId(Long animalId);

    List<Alarma> findByAnimalIdAndEstado(Long animalId, EstadoAlarma estado);

    List<Alarma> findByEsTratamientoMedicoTrueAndEstadoIn(List<EstadoAlarma> estados);
}
