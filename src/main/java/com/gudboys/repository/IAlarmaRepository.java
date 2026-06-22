package com.gudboys.repository;

import com.gudboys.domain.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAlarmaRepository extends JpaRepository<Alarma, Long> {

    List<Alarma> findByAnimalId(Long animalId);
}
