package com.gudboys.dto.request;

import com.gudboys.domain.enums.AccionAlarma;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CrearAlarmaRequestDTO {

    @NotNull(message = "La periodicidad es obligatoria")
    @Positive(message = "La periodicidad debe ser un valor positivo en dias")
    private Integer periodicidad;

    @NotNull(message = "Debe indicar si es tratamiento medico")
    private Boolean esTratamientoMedico;

    @NotEmpty(message = "Debe seleccionar al menos una accion")
    private List<AccionAlarma> acciones;
}
