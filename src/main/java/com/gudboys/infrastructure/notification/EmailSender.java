package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements IRecordatorioStrategy {

    @Override
    public String enviarRecordatorio(SeguimientoVisitas seguimiento) {
        // TODO: devolver un mensaje simulado, ej: "Se envió recordatorio por Email al seguimiento #" + seguimiento.getId()
        return "Se envió recordatorio por Email al seguimiento #" + seguimiento.getId();
    }

    @Override
    public String getCanal() {
        return "EMAIL";
    }
}
