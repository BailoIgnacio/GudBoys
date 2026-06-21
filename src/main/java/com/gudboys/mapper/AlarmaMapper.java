package com.gudboys.mapper;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.Animal;
import com.gudboys.domain.enums.EstadoAlarma;
import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AlarmaMapper {

    public Alarma toEntity(CrearAlarmaRequestDTO dto, Animal animal) {
        Alarma alarma = new Alarma();
        alarma.setPeriodicidad(dto.getPeriodicidad());
        alarma.setEsTratamientoMedico(dto.getEsTratamientoMedico());
        alarma.setAcciones(dto.getAcciones());
        alarma.setEstado(EstadoAlarma.ACTIVA);
        alarma.setAnimal(animal);
        return alarma;
    }

    public Alarma actualizarEntity(CrearAlarmaRequestDTO dto, Alarma alarma) {
        alarma.setPeriodicidad(dto.getPeriodicidad());
        alarma.setEsTratamientoMedico(dto.getEsTratamientoMedico());
        alarma.setAcciones(dto.getAcciones());

        return alarma;
    }

    public AlarmaResponseDTO toResponseDTO(Alarma alarma) {
        return AlarmaResponseDTO.builder()
                .id(alarma.getId())
                .animalId(alarma.getAnimal().getId())
                .periodicidad(alarma.getPeriodicidad())
                .esTratamientoMedico(alarma.isEsTratamientoMedico())
                .estado(alarma.getEstado())
                .acciones(alarma.getAcciones())
                .build();
    }
}
