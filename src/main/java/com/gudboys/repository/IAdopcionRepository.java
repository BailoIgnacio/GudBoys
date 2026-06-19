package com.gudboys.repository;

import com.gudboys.domain.Adopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAdopcionRepository extends JpaRepository<Adopcion, Long> {

    List<Adopcion> findByAdoptanteId(Long adoptanteId);

    Optional<Adopcion> findByAnimalId(Long animalId);

    long countByAdoptanteId(Long adoptanteId);
}
