package com.gudboys.domain;

import com.gudboys.domain.enums.PreferenciaRecordatorio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seguimientos_visitas")
@Getter
@Setter
@NoArgsConstructor
public class SeguimientoVisitas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopcion_id", nullable = false, unique = true)
    private Adopcion adopcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitador_id", nullable = false)
    private Visitador visitador;

    @Column(nullable = false)
    private LocalDate diaVisita;

    @Column(nullable = false)
    private String horarioVisita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenciaRecordatorio preferenciaRecordatorio;

    @OneToMany(mappedBy = "seguimientoVisitas", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("fechaVisita ASC")
    private List<VisitaDomicilio> visitas = new ArrayList<>();
}
