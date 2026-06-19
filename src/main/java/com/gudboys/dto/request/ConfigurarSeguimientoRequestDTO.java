package com.gudboys.dto.request;

import com.gudboys.domain.enums.PreferenciaRecordatorio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurarSeguimientoRequestDTO {

    @NotNull(message = "El ID de la adopcion es obligatorio")
    private Long adopcionId;

    @NotNull(message = "El ID del visitador responsable es obligatorio")
    private Long visitadorId;

    @NotNull(message = "El dia de visita es obligatorio")
    private LocalDate diaVisita;

    @NotBlank(message = "El horario de visita es obligatorio")
    private String horarioVisita;

    @NotNull(message = "La preferencia de recordatorio es obligatoria")
    private PreferenciaRecordatorio preferenciaRecordatorio;
}
