package com.gudboys.domain.state;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.enums.EstadoAlarma;

/**
 * Patrón State — estado del ciclo de vida de una Alarma.
 * Implementaciones: {@link EstadoActiva}, {@link EstadoAtendida}, {@link EstadoFinalizada}.
 *
 * Scaffolding generado (workflow tarea A.1). La LÓGICA de cada método la implementa el equipo.
 */
public interface IEstadoAlarma {

    /** Transiciona la alarma al atenderla. Cada estado decide el próximo estado. */
    void atender(Alarma alarma, boolean tratamientoFinalizado);

    /** Indica si, estando en este estado, la alarma bloquea la adopción del animal (RN-07). */
    boolean bloqueaAdopcion();

    /** Discriminador persistible del estado (para mapear a/desde la columna en BD). */
    EstadoAlarma getTipo();
}
