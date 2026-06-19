package com.gudboys.service;

import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;

import java.util.List;

public interface IGestionAlarmaService {

    AlarmaResponseDTO crearAlarma(Long animalId, CrearAlarmaRequestDTO dto);

    AlarmaResponseDTO actualizarAlarma(Long alarmaId, CrearAlarmaRequestDTO dto);

    List<AlarmaResponseDTO> listarAlarmasPorAnimal(Long animalId);
}
