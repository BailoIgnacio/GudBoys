package com.gudboys.domain;

import com.gudboys.domain.observer.IObservador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "veterinarios")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Veterinario extends Usuario implements IObservador {

    /** Observer: el veterinario recibe la alerta disparada por la alarma. */
    @Override
    public void actualizar(Alerta alerta) {
        log.info("[Observer] Veterinario {} ({}) notificado de la alerta {} (alarma {})",
                getId(), getNombre(), alerta.getId(),
                alerta.getAlarma() != null ? alerta.getAlarma().getId() : null);
    }
}
