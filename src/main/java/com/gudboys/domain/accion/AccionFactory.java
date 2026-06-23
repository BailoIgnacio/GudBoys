package com.gudboys.domain.accion;

import com.gudboys.domain.enums.AccionAlarma;

import java.util.List;

/**
 * Factory del patrón Composite: traduce el discriminador persistido ({@link AccionAlarma})
 * al árbol de objetos {@link ComponenteAccion}.
 *
 * Mantiene el enum como representación en BD y reconstruye las hojas/compuesto en memoria,
 * igual que el converter del patrón State con el estado de la alarma.
 */
public final class AccionFactory {

    private AccionFactory() {
    }

    /** Crea la hoja concreta que corresponde al enum. */
    public static ComponenteAccion crearHoja(AccionAlarma accion) {
        return switch (accion) {
            case CONTROL_PARASITOS        -> new ControlParasitosAccion();
            case COLOCAR_ANTIPARASITARIOS -> new ColocarAntiparasitarios();
            case COMPROBAR_PESO_TAMANIO   -> new ComprobarPesoTamanio();
            case CHEQUEAR_NUTRICION       -> new ChequearNutricion();
            case COLOCAR_VACUNA           -> new ColocarVacuna();
        };
    }

    /** Construye el compuesto a partir de la lista de enums persistida. */
    public static AccionComposite crearComposite(List<AccionAlarma> acciones) {
        AccionComposite composite = new AccionComposite();
        if (acciones != null) {
            acciones.forEach(a -> composite.agregar(crearHoja(a)));
        }
        return composite;
    }
}
