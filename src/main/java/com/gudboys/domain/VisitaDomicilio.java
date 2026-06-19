package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visitas_domicilio")
@Getter
@Setter
@NoArgsConstructor
public class VisitaDomicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
