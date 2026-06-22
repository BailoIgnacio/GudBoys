package com.gudboys.infrastructure.notification;

import com.gudboys.domain.enums.PreferenciaRecordatorio;

/**
 * Factory de estrategias de recordatorio (tarea B.5).
 * Devuelve la {@link IRecordatorioStrategy} correspondiente al canal preferido.
 */
public interface IRecordatorioFactory {

    IRecordatorioStrategy crear(PreferenciaRecordatorio preferencia);
}
