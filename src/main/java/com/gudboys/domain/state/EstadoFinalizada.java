package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class EstadoFinalizada implements IEstadoAlarma {

    @Override
    public void atender(Alarma alarma, boolean tratamientoFinalizado) {
        throw new RuntimeException("No puede saltar una alarma en un animal sin tratamiento");
    }

    @Override
    public boolean bloqueaAdopcion() {
        return false;
    }

    @Override
    public EstadoAlarma getTipo() {
        return EstadoAlarma.FINALIZADA;
    }
}
