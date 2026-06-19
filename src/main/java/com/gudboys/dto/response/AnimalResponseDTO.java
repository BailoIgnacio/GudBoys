package com.gudboys.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimalResponseDTO {

    private Long id;
    private String tipoAnimal;
    private String especie;
    private Double altura;
    private Double peso;
    private Double edad;
    private Boolean condicionMedica;
    private Boolean esAdoptable;
    private Long fichaMedicaId;
}
