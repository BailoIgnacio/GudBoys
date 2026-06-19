package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;
import org.springframework.stereotype.Component;

@Component
public class ExportadorPDF implements IExportadorStrategy {

    @Override
    public byte[] exportar(FichaMedica fichaMedica) {
        // TODO: implementar exportacion a PDF (usar iText o Apache PDFBox)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getFormato() {
        return "PDF";
    }
}
