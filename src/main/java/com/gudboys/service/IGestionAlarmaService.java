package com.gudboys.service;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.Alerta;
import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;

import java.util.List;

public interface IGestionAlarmaService {

    AlarmaResponseDTO crearAlarma(Long animalId, CrearAlarmaRequestDTO dto);

    AlarmaResponseDTO actualizarAlarma(Long alarmaId, CrearAlarmaRequestDTO dto);

    List<AlarmaResponseDTO> listarAlarmasPorAnimal(Long animalId);

    /** Observer: crea y persiste la alerta y notifica a todos los veterinarios + push. */
    Alerta generarAlerta(Alarma alarma);

    /** Scheduler: detecta las alarmas ACTIVA vencidas y dispara su alerta. Devuelve cuántas disparó. */
    int dispararAlarmasVencidas();
}
