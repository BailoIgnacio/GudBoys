package com.gudboys.service;

import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;

import java.util.List;

public interface IIngresoAnimalService {

    AnimalResponseDTO ingresarAnimal(IngresarAnimalRequestDTO dto);

    AnimalResponseDTO obtenerAnimal(Long id);

    List<AnimalResponseDTO> listarAnimales();

    FichaMedicaResponseDTO obtenerFichaMedica(Long animalId);
}
