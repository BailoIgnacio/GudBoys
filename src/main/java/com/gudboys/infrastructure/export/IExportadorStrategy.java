package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;

public interface IExportadorStrategy {

    byte[] exportar(FichaMedica fichaMedica);

    String getFormato();
}
