package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "animales_salvajes")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class AnimalSalvaje extends Animal {

    @Column(nullable = false)
    private String especie;

    @Override
    public boolean esAdoptable() {
        return false;
    }
}
