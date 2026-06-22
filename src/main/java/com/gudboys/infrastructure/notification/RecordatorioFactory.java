package com.gudboys.infrastructure.notification;

import com.gudboys.domain.enums.PreferenciaRecordatorio;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementación del factory de recordatorios (tarea B.5).
 *
 * Spring inyecta todas las IRecordatorioStrategy (@Component): SmsSender,
 * WhatsAppSender, EmailSender. La selección se hace por su getCanal().
 */
@Component
public class RecordatorioFactory implements IRecordatorioFactory {

    private final List<IRecordatorioStrategy> estrategias;

    public RecordatorioFactory(List<IRecordatorioStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    @Override
    public IRecordatorioStrategy crear(PreferenciaRecordatorio preferencia) {
        // TODO B.5: elegir la estrategia cuyo getCanal() coincida con la preferencia
        //  (SMS / WHATSAPP / EMAIL) y devolverla
        throw new UnsupportedOperationException("TODO B.5: seleccionar estrategia por canal");
    }
}
