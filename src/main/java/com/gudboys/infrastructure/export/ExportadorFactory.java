package com.gudboys.infrastructure.export;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory de exportadores (tarea B.3). Arma la cadena Strategy + Decorators
 * según el formato y los flags de encriptado / marca de agua (RN-15).
 *
 * Spring inyecta los exportadores BASE (@Component): ExportadorPDF, ExportadorExcel.
 * Los decoradores NO son beans: se instancian acá envolviendo el base.
 */
@Component
public class ExportadorFactory {

    private final List<IExportadorStrategy> exportadoresBase;

    public ExportadorFactory(List<IExportadorStrategy> exportadoresBase) {
        this.exportadoresBase = exportadoresBase;
    }

    /**
     * @param formato   "PDF" o "EXCEL" (matchea con IExportadorStrategy.getFormato())
     * @param encriptar si true, envuelve con EncriptarDecorator
     * @param agua      si true, envuelve con MarcaAguaDecorator
     */
    public IExportadorStrategy crearExportador(String formato, boolean encriptar, boolean agua) {
        // TODO B.3:
        //  1. elegir el exportador base cuyo getFormato() coincida con `formato`
        //  2. si `encriptar` -> new EncriptarDecorator(base)
        //  3. si `agua`      -> new MarcaAguaDecorator(...)
        //  4. devolver el exportador resultante
        throw new UnsupportedOperationException("TODO B.3: armar strategy + decoradores");
    }
}
