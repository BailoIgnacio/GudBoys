package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visitas_domicilio")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class VisitaDomicilio extends Evento {

    // id, descripcion, fechaHora y fichaMedica se heredan de Evento (RN-14: la visita
    // queda en el historial de la ficha medica del animal).

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguimiento_id", nullable = false)
    private SeguimientoVisitas seguimientoVisitas;

    @Column(nullable = false)
    private LocalDate fechaVisita;

    @Column(nullable = false)
    private Boolean continuarVisitas;

    @OneToOne(mappedBy = "visitaDomicilio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EncuestaSeguimiento encuesta;
}
