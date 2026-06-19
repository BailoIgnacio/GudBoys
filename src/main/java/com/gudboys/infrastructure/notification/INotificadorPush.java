package com.gudboys.infrastructure.notification;

import com.gudboys.domain.Alerta;
import com.gudboys.domain.Veterinario;

import java.util.List;

public interface INotificadorPush {

    void notificar(Alerta alerta, List<Veterinario> veterinarios);
}
