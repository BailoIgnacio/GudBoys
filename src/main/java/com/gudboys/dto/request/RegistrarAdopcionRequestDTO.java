package com.gudboys.dto.request;

import com.gudboys.domain.enums.Ocupacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarAdopcionRequestDTO {

    @NotNull(message = "El ID del animal es obligatorio")
    private Long animalId;

    @NotBlank(message = "El nombre del adoptante es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido del adoptante es obligatorio")
    private String apellido;

    private String estadoCivil;

    @NotBlank(message = "El email del adoptante es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    private String email;

    private String telefono;

    @NotNull(message = "La ocupacion del adoptante es obligatoria")
    private Ocupacion ocupacion;

    @NotNull(message = "Debe indicar si el adoptante tiene otras mascotas")
    private Boolean tieneMascotas;

    private String motivoAdopcion;

    private List<String> tiposAnimalesInteresados;
}
