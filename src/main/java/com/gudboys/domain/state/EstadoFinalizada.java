package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;

/**
 * Estado FINALIZADA (terminal). Scaffolding (tarea A.1) — completar la lógica.
 */
public class EstadoFinalizada implements IEstadoAlarma {

    @Override
    public void atender(Alarma alarma, boolean tratamientoFinalizado) {
        // TODO A.1: estado terminal -> normalmente no admite más transiciones (¿lanzar excepción?)
        throw new UnsupportedOperationException("TODO A.1: FINALIZADA es estado terminal");
    }

    @Override
    public boolean bloqueaAdopcion() {
        // FINALIZADA no bloquea adopción
        return false;
    }

    @Override
    public EstadoAlarma getTipo() {
        return EstadoAlarma.FINALIZADA;
    }
}
