package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;

public interface IRecordatorioStrategy {

    void enviarRecordatorio(SeguimientoVisitas seguimiento);

    String getCanal();
}
