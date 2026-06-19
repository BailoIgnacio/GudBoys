package com.gudboys.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EventoResponseDTO {

    private Long id;
    private String tipo;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String comentario;
    private Boolean tratamientoFinalizado;
    private String nombreVeterinario;
}
