package com.gudboys.domain.accion;

import java.util.ArrayList;
import java.util.List;

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
        // TODO A.2: agregar las descripciones de los componentes hijos
        throw new UnsupportedOperationException("TODO A.2: descripción agregada del compuesto");
    }
}
