package com.gudboys.domain;

import com.gudboys.domain.enums.AccionAlarma;
import com.gudboys.domain.state.EstadoActiva;
import com.gudboys.domain.state.EstadoAlarmaConverter;
import com.gudboys.domain.state.IEstadoAlarma;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alarmas")
@Getter
@Setter
@NoArgsConstructor
public class Alarma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer periodicidad;

    @Column(nullable = false)
    private boolean esTratamientoMedico;

    @Convert(converter = EstadoAlarmaConverter.class)
    @Column(nullable = false)
    private IEstadoAlarma estado = new EstadoActiva();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "alarma_acciones", joinColumns = @JoinColumn(name = "alarma_id"))
    @Column(name = "accion")
    private List<AccionAlarma> acciones = new ArrayList<>();
}
