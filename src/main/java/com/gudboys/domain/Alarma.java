package com.gudboys.domain;

import com.gudboys.domain.accion.AccionComposite;
import com.gudboys.domain.accion.AccionFactory;
import com.gudboys.domain.enums.AccionAlarma;
import com.gudboys.domain.state.EstadoActiva;
import com.gudboys.domain.state.EstadoAlarmaConverter;
import com.gudboys.domain.state.IEstadoAlarma;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    /** Fecha del próximo disparo automático (A.4). El scheduler la usa para detectar alarmas vencidas. */
    @Column
    private LocalDateTime fechaProximoDisparo;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "alarma_acciones", joinColumns = @JoinColumn(name = "alarma_id"))
    @Column(name = "accion")
    private List<AccionAlarma> acciones = new ArrayList<>();

    /**
     * Vista Composite de las acciones. El enum se mantiene como persistencia (discriminador)
     * y el árbol de {@link com.gudboys.domain.accion.ComponenteAccion} se reconstruye en memoria.
     */
    @Transient
    public AccionComposite getAccionesComposite() {
        return AccionFactory.crearComposite(acciones);
    }
}
