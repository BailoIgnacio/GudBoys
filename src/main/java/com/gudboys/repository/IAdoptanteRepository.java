package com.gudboys.repository;

import com.gudboys.domain.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdoptanteRepository extends JpaRepository<Adoptante, Long> {

    Optional<Adoptante> findByEmail(String email);
}
