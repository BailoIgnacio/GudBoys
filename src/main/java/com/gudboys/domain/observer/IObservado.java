package com.gudboys.domain.observer;

import com.gudboys.domain.Alerta;

/**
 * Patrón Observer — sujeto observado. Gestiona la lista de observadores y los notifica.
 * Scaffolding (tarea A.3).
 */
public interface IObservado {

    void suscribir(IObservador observador);

    void desuscribir(IObservador observador);

    void notificarObservadores(Alerta alerta);
}
