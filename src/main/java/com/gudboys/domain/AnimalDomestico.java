package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "animales_domesticos")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class AnimalDomestico extends Animal {

    @Column(nullable = false)
    private String especie;

    @Override
    public boolean esAdoptable() {
        boolean tieneAlarmaActivaConTratamiento = getAlarmas().stream()
                .anyMatch(a -> a.isEsTratamientoMedico()
                        && a.getEstado().bloqueaAdopcion());
        return !tieneAlarmaActivaConTratamiento;
    }
}
