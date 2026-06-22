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
    public byte[] exportar(FichaMedica fichaMedica) {
        byte[] base = exportadorEnvuelto.exportar(fichaMedica);
        // TODO B.2: agregar marca de agua a `base` y devolver el resultado
        throw new UnsupportedOperationException("TODO B.2: marca de agua en la exportación");
    }
}
