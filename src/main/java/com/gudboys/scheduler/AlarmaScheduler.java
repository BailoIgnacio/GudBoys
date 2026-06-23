package com.gudboys.scheduler;

import com.gudboys.service.IGestionAlarmaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler de disparo automático de alarmas (tarea A.4).
 *
 * Detecta las alarmas vencidas y, por cada una ACTIVA, dispara su alerta (patrón Observer).
 * La lógica vive en {@link IGestionAlarmaService#dispararAlarmasVencidas()}; el scheduler solo es el disparador.
 *
 * El intervalo se configura con la property gudboys.scheduler.alarmas.fixed-delay (ms).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmaScheduler {

    private final IGestionAlarmaService gestionAlarmaService;

    @Scheduled(fixedDelayString = "${gudboys.scheduler.alarmas.fixed-delay:3600000}")
    public void dispararAlarmasVencidas() {
        int disparadas = gestionAlarmaService.dispararAlarmasVencidas();
        if (disparadas > 0) {
            log.info("[Scheduler] Alarmas vencidas disparadas: {}", disparadas);
        }
    }
}
