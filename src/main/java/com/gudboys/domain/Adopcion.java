package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "adopciones")
@Getter
@Setter
@NoArgsConstructor
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoptante_id", nullable = false)
    private Adoptante adoptante;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false, unique = true)
    private AnimalDomestico animal;

    @Column(nullable = false)
    private LocalDate fechaAdopcion;

    @OneToOne(mappedBy = "adopcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SeguimientoVisitas seguimientoVisitas;

    @PrePersist
    protected void onCreate() {
        if (fechaAdopcion == null) {
            fechaAdopcion = LocalDate.now();
        }
    }
}
