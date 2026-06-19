package com.gudboys.repository;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ISeguimientoVisitasRepository extends JpaRepository<SeguimientoVisitas, Long> {

    Optional<SeguimientoVisitas> findByAdopcionId(Long adopcionId);

    @Query("SELECT s FROM SeguimientoVisitas s WHERE s.diaVisita BETWEEN :desde AND :hasta")
    List<SeguimientoVisitas> findVisitasEntreFechas(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
