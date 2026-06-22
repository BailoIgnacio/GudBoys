package com.gudboys.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler de disparo automático de alarmas (tarea A.4).
 *
 * Scaffolding: el @Scheduled ya está cableado. Falta la LÓGICA:
 *  - Agregar a Alarma un campo de fecha (p. ej. fechaProximoDisparo) — hoy NO existe.
 *  - Agregar al IAlarmaRepository una query de alarmas vencidas.
 *  - Inyectar el service que genera la alerta (Observer, tarea A.3) y llamarlo acá.
 *
 * El intervalo se configura con la property gudboys.scheduler.alarmas.fixed-delay (ms).
 */
@Component
public class AlarmaScheduler {

    @Scheduled(fixedDelayString = "${gudboys.scheduler.alarmas.fixed-delay:3600000}")
    public void dispararAlarmasVencidas() {
        // TODO A.4: buscar alarmas vencidas y generar la alerta (Observer)
    }
}
