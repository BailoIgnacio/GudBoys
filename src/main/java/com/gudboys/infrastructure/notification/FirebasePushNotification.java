package com.gudboys.infrastructure.notification;

import com.gudboys.domain.Alerta;
import com.gudboys.domain.observer.INotificadorPushObservador;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Canal de notificación push (Firebase) como observador del patrón Observer.
 * Envío simulado por log — alcanza para el TP; reemplazar por Firebase Cloud Messaging real si se requiere.
 */
@Component
@Slf4j
public class FirebasePushNotification implements INotificadorPushObservador {

    @Override
    public void actualizar(Alerta alerta) {
        log.info("[Firebase PUSH simulado] Enviando notificación push por alerta {} (alarma {})",
                alerta.getId(),
                alerta.getAlarma() != null ? alerta.getAlarma().getId() : null);
    }
}
