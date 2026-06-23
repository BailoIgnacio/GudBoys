package com.gudboys.infrastructure.export;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory de exportadores (tarea B.3). Arma la cadena Strategy + Decorators
 * según el formato y los flags de encriptado / marca de agua (RN-15).
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

        // 1. Buscar entre los exportadores base (PDF, Excel) el que matchea el formato pedido
        IExportadorStrategy exportador = exportadoresBase.stream()
                .filter(base -> base.getFormato().equalsIgnoreCase(formato))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato de exportación no soportado: " + formato));

        // 2. Envolver con EncriptarDecorator si corresponde
        if (encriptar) {
            exportador = new EncriptarDecorator(exportador);
        }

        // 3. Envolver con MarcaAguaDecorator si corresponde (se aplica sobre lo que haya quedado hasta acá)
        if (agua) {
            exportador = new MarcaAguaDecorator(exportador);
        }

        // 4. Devolver el exportador final (base solo, o envuelto en 1 o 2 decoradores)
        return exportador;
    }
}
