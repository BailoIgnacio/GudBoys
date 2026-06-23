package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;
import org.springframework.stereotype.Component;

@Component
public class ExportadorExcel implements IExportadorStrategy {

    @Override
    public String exportar(FichaMedica fichaMedica) {
        return "Se exportó la ficha médica #" + fichaMedica.getId() + " en formato " + getFormato();
    }

    @Override
    public String getFormato() {
        return "EXCEL";
    }
}
