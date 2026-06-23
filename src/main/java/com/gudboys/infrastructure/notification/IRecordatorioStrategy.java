package com.gudboys.infrastructure.notification;

import com.gudboys.domain.SeguimientoVisitas;

public interface IRecordatorioStrategy {

    String enviarRecordatorio(SeguimientoVisitas seguimiento);

    String getCanal();
}
