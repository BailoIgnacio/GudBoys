package com.gudboys.scheduler;

import com.gudboys.service.IVisitaSeguimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler de recordatorios de visita (tarea B.6).
 *
 * Scaffolding: ya está cableado y delega en el service. La LÓGICA vive en
 * IVisitaSeguimientoService.enviarRecordatoriosProximos() (tarea B.5).
 *
 * El cron se configura con la property gudboys.scheduler.recordatorios.cron.
 */
@Component
@RequiredArgsConstructor
public class RecordatorioScheduler {

    private final IVisitaSeguimientoService visitaSeguimientoService;

    @Scheduled(cron = "${gudboys.scheduler.recordatorios.cron:0 0 8 * * *}")
    public void enviarRecordatorios() {
        visitaSeguimientoService.enviarRecordatoriosProximos();
    }
}
