package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;

/**
 * Decorator que agrega una marca de agua a la salida del exportador envuelto (RN-15).
 * Scaffolding (tarea B.2) — completar la lógica de marca de agua.
 */
public class MarcaAguaDecorator extends ExportadorDecorator {

    public MarcaAguaDecorator(IExportadorStrategy exportadorEnvuelto) {
        super(exportadorEnvuelto);
    }

    @Override
    public String exportar(FichaMedica fichaMedica) {
        String base = exportadorEnvuelto.exportar(fichaMedica);
        return base + " (con marca de agua)";
    }
}
