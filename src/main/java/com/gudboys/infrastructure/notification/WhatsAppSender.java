package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;
import org.springframework.stereotype.Component;

@Component
public class WhatsAppSender implements IRecordatorioStrategy {

    @Override
    public void enviarRecordatorio(SeguimientoVisitas seguimiento) {
        // TODO: implementar envio de recordatorio por WhatsApp
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getCanal() {
        return "WHATSAPP";
    }
}
