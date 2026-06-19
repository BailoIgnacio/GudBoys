package com.gudboys.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FichaMedicaResponseDTO {

    private Long id;
    private Long animalId;
    private String especieAnimal;
    private Boolean bajoTratamientoActivo;
    private List<EventoResponseDTO> eventos;
}
