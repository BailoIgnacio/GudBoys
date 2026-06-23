package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;

/**
 * Decorator que encripta la salida del exportador envuelto (RN-15).
 * Scaffolding (tarea B.2) — completar la lógica de encriptado.
 */
public class EncriptarDecorator extends ExportadorDecorator {

    public EncriptarDecorator(IExportadorStrategy exportadorEnvuelto) {
        super(exportadorEnvuelto);
    }

    @Override
    public String exportar(FichaMedica fichaMedica) {
        String base = exportadorEnvuelto.exportar(fichaMedica);
        return base + " (encriptado)";
    }
}
