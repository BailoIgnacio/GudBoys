package com.gudboys.repository;

import com.gudboys.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByAtendidaFalse();

    List<Alerta> findByAlarmaId(Long alarmaId);
}
