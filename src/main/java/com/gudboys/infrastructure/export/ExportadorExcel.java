package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;
import org.springframework.stereotype.Component;

@Component
public class ExportadorExcel implements IExportadorStrategy {

    @Override
    public byte[] exportar(FichaMedica fichaMedica) {
        // TODO: implementar exportacion a Excel (usar Apache POI)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getFormato() {
        return "EXCEL";
    }
}
