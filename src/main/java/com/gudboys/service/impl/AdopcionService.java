package com.gudboys.service.impl;

import com.gudboys.dto.request.RegistrarAdopcionRequestDTO;
import com.gudboys.dto.response.AdopcionResponseDTO;
import com.gudboys.mapper.AdopcionMapper;
import com.gudboys.repository.IAdopcionRepository;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.service.IAdopcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdopcionService implements IAdopcionService {

    private final IAdopcionRepository adopcionRepository;
    private final IAnimalRepository animalRepository;
    private final AdopcionMapper adopcionMapper;

    @Override
    public AdopcionResponseDTO registrarAdopcion(RegistrarAdopcionRequestDTO dto) {
        // TODO: implementar logica de registro de adopcion
        // Debe validar: animal adoptable (RN-07, RN-08), limite 2 adopciones por adoptante (RN-09)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AdopcionResponseDTO obtenerAdopcion(Long id) {
        // TODO: implementar logica de busqueda de adopcion
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AdopcionResponseDTO> listarAdopciones() {
        // TODO: implementar logica de listado de adopciones
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
