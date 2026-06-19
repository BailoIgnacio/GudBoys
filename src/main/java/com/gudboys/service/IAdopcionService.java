package com.gudboys.service;

import com.gudboys.dto.request.RegistrarAdopcionRequestDTO;
import com.gudboys.dto.response.AdopcionResponseDTO;

import java.util.List;

public interface IAdopcionService {

    AdopcionResponseDTO registrarAdopcion(RegistrarAdopcionRequestDTO dto);

    AdopcionResponseDTO obtenerAdopcion(Long id);

    List<AdopcionResponseDTO> listarAdopciones();
}
