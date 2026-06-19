package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarma_id", nullable = false)
    private Alarma alarma;

    @Column(nullable = false)
    private LocalDateTime fechaDisparo;

    @Column(nullable = false)
    private Boolean atendida = false;

    @PrePersist
    protected void onCreate() {
        if (fechaDisparo == null) {
            fechaDisparo = LocalDateTime.now();
        }
    }
}
