package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;

/**
 * Estado ACTIVA. Scaffolding (tarea A.1) — completar la lógica de transición/bloqueo.
 */
public class EstadoActiva implements IEstadoAlarma {

    @Override
    public void atender(Alarma alarma, boolean tratamientoFinalizado) {
        // TODO A.1: si es tratamiento y finalizó -> FINALIZADA; si no -> ATENDIDA
        throw new UnsupportedOperationException("TODO A.1: transición desde ACTIVA");
    }

    @Override
    public boolean bloqueaAdopcion() {
        // TODO A.1: una alarma de tratamiento ACTIVA bloquea la adopción
        throw new UnsupportedOperationException("TODO A.1: bloqueaAdopcion en ACTIVA");
    }

    @Override
    public EstadoAlarma getTipo() {
        return EstadoAlarma.ACTIVA;
    }
}
