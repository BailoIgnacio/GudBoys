package com.gudboys.dto.response;

import com.gudboys.domain.enums.PreferenciaRecordatorio;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SeguimientoResponseDTO {

    private Long id;
    private Long adopcionId;
    private Long visitadorId;
    private String nombreVisitador;
    private LocalDate diaVisita;
    private String horarioVisita;
    private PreferenciaRecordatorio preferenciaRecordatorio;
    private Integer cantidadVisitasRealizadas;
}
