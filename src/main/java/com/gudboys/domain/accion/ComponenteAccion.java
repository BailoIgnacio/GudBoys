package com.gudboys.domain.accion;

/**
 * Patrón Composite — componente común de las acciones de una alarma.
 * Compuesto: {@link AccionComposite}. Hojas: una clase por acción.
 *
 * Scaffolding (tarea A.2). El equipo define la operación común definitiva y su lógica.
 */
public interface ComponenteAccion {

    /** Descripción legible de la acción (o agregada, en el caso del compuesto). */
    String getDescripcion();
}
