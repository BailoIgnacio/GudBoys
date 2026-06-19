package com.gudboys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngresarAnimalRequestDTO {

    @NotBlank(message = "El tipo de animal es obligatorio (DOMESTICO o SALVAJE)")
    private String tipoAnimal;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    @NotNull(message = "La altura es obligatoria")
    @Positive
    private Double altura;

    @NotNull(message = "El peso es obligatorio")
    @Positive
    private Double peso;

    @NotNull(message = "La edad es obligatoria")
    @Positive
    private Double edad;

    @NotNull(message = "La condicion medica es obligatoria")
    private Boolean condicionMedica;
}
