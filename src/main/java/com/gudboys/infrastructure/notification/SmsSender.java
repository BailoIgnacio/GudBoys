package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.stereotype.Component;

@Component
public class SmsSender implements IRecordatorioStrategy {

    @Override
    public void enviarRecordatorio(SeguimientoVisitas seguimiento) {
        // TODO: implementar envio de recordatorio por SMS
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getCanal() {
        return "SMS";
    }
}
