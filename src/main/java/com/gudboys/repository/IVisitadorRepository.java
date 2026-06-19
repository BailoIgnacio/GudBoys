package com.gudboys.repository;

import com.gudboys.domain.Visitador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVisitadorRepository extends JpaRepository<Visitador, Long> {

    Optional<Visitador> findByExternalId(String externalId);
}
