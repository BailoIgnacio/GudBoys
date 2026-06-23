package com.gudboys.domain.accion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Compuesto del patrón Composite: agrupa varias {@link ComponenteAccion}.
 * Scaffolding (tarea A.2) — completar la lógica de agregación.
 */
public class AccionComposite implements ComponenteAccion {

    private final List<ComponenteAccion> acciones = new ArrayList<>();

    public void agregar(ComponenteAccion accion) {
        acciones.add(accion);
    }

    public void quitar(ComponenteAccion accion) {
        acciones.remove(accion);
    }

    public List<ComponenteAccion> getAcciones() {
        return acciones;
    }

    @Override
    public String getDescripcion() {
        // Composite: pide la descripción a cada hijo (hoja u otro compuesto) y las agrega.
        return acciones.stream()
                .map(ComponenteAccion::getDescripcion)
                .collect(Collectors.joining(", "));
    }
}
