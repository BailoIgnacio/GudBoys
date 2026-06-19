package com.gudboys.service.impl;

import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;
import com.gudboys.mapper.AnimalMapper;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.repository.IFichaMedicaRepository;
import com.gudboys.service.IIngresoAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngresoAnimalService implements IIngresoAnimalService {

    private final IAnimalRepository animalRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalResponseDTO ingresarAnimal(IngresarAnimalRequestDTO dto) {
        // TODO: implementar logica de ingreso de animal
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AnimalResponseDTO obtenerAnimal(Long id) {
        // TODO: implementar logica de busqueda de animal
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AnimalResponseDTO> listarAnimales() {
        // TODO: implementar logica de listado de animales
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public FichaMedicaResponseDTO obtenerFichaMedica(Long animalId) {
        // TODO: implementar logica de obtencion de ficha medica
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
