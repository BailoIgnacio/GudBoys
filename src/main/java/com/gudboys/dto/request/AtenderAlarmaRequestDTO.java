package com.gudboys.dto.request;

import com.gudboys.domain.enums.AccionAlarma;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AtenderAlarmaRequestDTO {

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long veterinarioId;

    @NotEmpty(message = "Debe indicar las acciones realizadas")
    private List<AccionAlarma> accionesRealizadas;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;

    private Boolean tratamientoFinalizado;
}
