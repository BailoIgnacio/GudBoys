package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class EstadoAtendida implements IEstadoAlarma {

    @Override
    public void atender(Alarma alarma, boolean tratamientoFinalizado) {
        if(alarma.isEsTratamientoMedico() && tratamientoFinalizado){
            alarma.setEstado(new EstadoFinalizada());
        } else{
            alarma.setEstado(new EstadoAtendida());
        }
    }

    @Override
    public boolean bloqueaAdopcion() {
        return true;
    }

    @Override
    public EstadoAlarma getTipo() {
        return EstadoAlarma.ATENDIDA;
    }
}
