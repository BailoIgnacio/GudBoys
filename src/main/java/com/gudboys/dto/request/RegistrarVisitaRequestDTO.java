package com.gudboys.dto.request;

import com.gudboys.domain.enums.Calificacion;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarVisitaRequestDTO {

    @NotNull(message = "La fecha de visita es obligatoria")
    private LocalDate fechaVisita;

    @NotNull(message = "El estado general del animal es obligatorio")
    private Calificacion estadoGeneral;

    @NotNull(message = "La limpieza del lugar es obligatoria")
    private Calificacion limpiezaLugar;

    @NotNull(message = "El ambiente es obligatorio")
    private Calificacion ambiente;

    @NotNull(message = "Debe indicar si se continuan las visitas")
    private Boolean continuarVisitas;
}
