package com.gudboys.domain;

import com.gudboys.domain.observer.IObservado;
import com.gudboys.domain.observer.IObservador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
public class Alerta implements IObservado {

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

    /** Observadores suscritos (veterinarios + canal push). No se persiste. */
    @Transient
    private final List<IObservador> observadores = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaDisparo == null) {
            fechaDisparo = LocalDateTime.now();
        }
    }

    @Override
    public void suscribir(IObservador observador) {
        observadores.add(observador);
    }

    @Override
    public void desuscribir(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(Alerta alerta) {
        observadores.forEach(o -> o.actualizar(alerta));
    }

    /** Dispara la notificación de esta alerta a todos los observadores suscritos. */
    public void alertarVeterinarios() {
        notificarObservadores(this);
    }
}
