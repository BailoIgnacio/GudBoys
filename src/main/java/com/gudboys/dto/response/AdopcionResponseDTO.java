package com.gudboys.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AdopcionResponseDTO {

    private Long id;
    private Long animalId;
    private String especieAnimal;
    private Long adoptanteId;
    private String nombreAdoptante;
    private String apellidoAdoptante;
    private LocalDate fechaAdopcion;
    private Long seguimientoId;
}
