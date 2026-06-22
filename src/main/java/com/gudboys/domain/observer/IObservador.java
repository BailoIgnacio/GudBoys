package com.gudboys.domain.observer;

import com.gudboys.domain.Alerta;

/**
 * Patrón Observer — observador. Lo implementa el Veterinario (recibe la alerta).
 * Scaffolding (tarea A.3).
 */
public interface IObservador {

    /** Se invoca cuando el observado (alarma) dispara una alerta. */
    void actualizar(Alerta alerta);
}
