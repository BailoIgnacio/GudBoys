package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements IRecordatorioStrategy {

    @Override
    public void enviarRecordatorio(SeguimientoVisitas seguimiento) {
        // TODO: implementar envio de recordatorio por Email (usar Spring Mail o similar)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getCanal() {
        return "EMAIL";
    }
}
