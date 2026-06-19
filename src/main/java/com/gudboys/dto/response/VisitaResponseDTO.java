package com.gudboys.dto.response;

import com.gudboys.domain.enums.Calificacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VisitaResponseDTO {

    private Long id;
    private Long seguimientoId;
    private LocalDate fechaVisita;
    private Boolean continuarVisitas;
    private Calificacion estadoGeneral;
    private Calificacion limpiezaLugar;
    private Calificacion ambiente;
}
