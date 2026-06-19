package com.gudboys.infrastructure.notification;

import com.gudboys.domain.Alerta;
import com.gudboys.domain.Veterinario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirebasePushNotification implements INotificadorPush {

    @Override
    public void notificar(Alerta alerta, List<Veterinario> veterinarios) {
        // TODO: implementar envio de push notification via Firebase Cloud Messaging
        // a todos los veterinarios cuando se dispara una alarma
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
