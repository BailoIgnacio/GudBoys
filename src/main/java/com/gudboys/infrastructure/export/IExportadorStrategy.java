package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;

public interface IExportadorStrategy {

    String exportar(FichaMedica fichaMedica);

    String getFormato();
}
