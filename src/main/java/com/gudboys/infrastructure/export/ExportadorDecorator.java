package com.gudboys.infrastructure.export;

import com.gudboys.domain.FichaMedica;

/**
 * Patrón Decorator — base abstracta para decorar un {@link IExportadorStrategy}.
 * Decoradores concretos: {@link EncriptarDecorator}, {@link MarcaAguaDecorator}.
 *
 * Scaffolding (tarea B.2). NO se registra como @Component (se instancia desde
 * {@link ExportadorFactory} envolviendo un exportador base).
 *
 * Nota: el diagrama lo modela como interfaz; se implementa como clase abstracta,
 * que es la forma estándar del patrón Decorator (mantiene la referencia al envuelto).
 */
public abstract class ExportadorDecorator implements IExportadorStrategy {

    protected final IExportadorStrategy exportadorEnvuelto;

    protected ExportadorDecorator(IExportadorStrategy exportadorEnvuelto) {
        this.exportadorEnvuelto = exportadorEnvuelto;
    }

    @Override
    public String getFormato() {
        return exportadorEnvuelto.getFormato();
    }

    @Override
    public abstract String exportar(FichaMedica fichaMedica);
}
