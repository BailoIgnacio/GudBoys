package com.gudboys.repository;

import com.gudboys.domain.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFichaMedicaRepository extends JpaRepository<FichaMedica, Long> {

    Optional<FichaMedica> findByAnimalId(Long animalId);
}
