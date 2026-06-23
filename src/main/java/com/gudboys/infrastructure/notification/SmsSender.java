package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.stereotype.Component;

@Component
public class SmsSender implements IRecordatorioStrategy {

    @Override
    public String enviarRecordatorio(SeguimientoVisitas seguimiento) {
        // TODO: devolver un mensaje simulado, ej: "Se envió recordatorio por SMS al seguimiento #" + seguimiento.getId()
        return "Se envió recordatorio por SMS al seguimiento #" + seguimiento.getId();
    }

    @Override
    public String getCanal() {
        return "SMS";
    }
}
