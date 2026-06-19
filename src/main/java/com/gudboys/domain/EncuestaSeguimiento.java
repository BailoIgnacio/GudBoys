package com.gudboys.domain;

import com.gudboys.domain.enums.Calificacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "encuestas_seguimiento")
@Getter
@Setter
@NoArgsConstructor
public class EncuestaSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visita_id", nullable = false, unique = true)
    private VisitaDomicilio visitaDomicilio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Calificacion estadoGeneral;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Calificacion limpiezaLugar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Calificacion ambiente;
}
