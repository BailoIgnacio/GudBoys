package com.gudboys.repository;

import com.gudboys.domain.Animal;
import com.gudboys.domain.AnimalDomestico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT a FROM AnimalDomestico a")
    List<AnimalDomestico> findAllDomesticos();

    @Query("SELECT a FROM Animal a WHERE TYPE(a) = AnimalDomestico AND a.id NOT IN " +
           "(SELECT ad.animal.id FROM Adopcion ad)")
    List<AnimalDomestico> findDomesticosDisponiblesParaAdopcion();
}
