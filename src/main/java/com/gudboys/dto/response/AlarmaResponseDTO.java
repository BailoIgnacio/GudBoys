package com.gudboys.dto.response;

import com.gudboys.domain.enums.AccionAlarma;
import com.gudboys.domain.enums.EstadoAlarma;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AlarmaResponseDTO {

    private Long id;
    private Long animalId;
    private Integer periodicidad;
    private Boolean esTratamientoMedico;
    private EstadoAlarma estado;
    private List<AccionAlarma> acciones;
}
