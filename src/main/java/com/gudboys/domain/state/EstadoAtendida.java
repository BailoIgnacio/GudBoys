package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;

/**
 * Estado ATENDIDA. Scaffolding (tarea A.1) — completar la lógica.
 */
public class EstadoAtendida implements IEstadoAlarma {

    @Override
    public void atender(Alarma alarma, boolean tratamientoFinalizado) {
        // TODO A.1: desde ATENDIDA, al finalizar el tratamiento -> FINALIZADA
        throw new UnsupportedOperationException("TODO A.1: transición desde ATENDIDA");
    }

    @Override
    public boolean bloqueaAdopcion() {
        // TODO A.1: una alarma de tratamiento ATENDIDA (sin finalizar) sigue bloqueando
        throw new UnsupportedOperationException("TODO A.1: bloqueaAdopcion en ATENDIDA");
    }

    @Override
    public EstadoAlarma getTipo() {
        return EstadoAlarma.ATENDIDA;
    }
}
