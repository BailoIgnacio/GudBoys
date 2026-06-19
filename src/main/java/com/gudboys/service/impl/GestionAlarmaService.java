package com.gudboys.service.impl;

import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import com.gudboys.mapper.AlarmaMapper;
import com.gudboys.repository.IAlarmaRepository;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.service.IGestionAlarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionAlarmaService implements IGestionAlarmaService {

    private final IAlarmaRepository alarmaRepository;
    private final IAnimalRepository animalRepository;
    private final AlarmaMapper alarmaMapper;

    @Override
    public AlarmaResponseDTO crearAlarma(Long animalId, CrearAlarmaRequestDTO dto) {
        // TODO: implementar logica de creacion de alarma
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AlarmaResponseDTO actualizarAlarma(Long alarmaId, CrearAlarmaRequestDTO dto) {
        // TODO: implementar logica de actualizacion de alarma
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AlarmaResponseDTO> listarAlarmasPorAnimal(Long animalId) {
        // TODO: implementar logica de listado de alarmas por animal
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
