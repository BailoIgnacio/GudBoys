package com.gudboys.mapper;

import com.gudboys.domain.Adopcion;
import com.gudboys.domain.EncuestaSeguimiento;
import com.gudboys.domain.SeguimientoVisitas;
import com.gudboys.domain.VisitaDomicilio;
import com.gudboys.domain.Visitador;
import com.gudboys.dto.request.ConfigurarSeguimientoRequestDTO;
import com.gudboys.dto.request.RegistrarVisitaRequestDTO;
import com.gudboys.dto.response.SeguimientoResponseDTO;
import com.gudboys.dto.response.VisitaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SeguimientoMapper {

    public SeguimientoVisitas toEntity(ConfigurarSeguimientoRequestDTO dto, Adopcion adopcion, Visitador visitador) {
        SeguimientoVisitas seguimiento = new SeguimientoVisitas();
        seguimiento.setAdopcion(adopcion);
        seguimiento.setVisitador(visitador);
        seguimiento.setDiaVisita(dto.getDiaVisita());
        seguimiento.setHorarioVisita(dto.getHorarioVisita());
        seguimiento.setPreferenciaRecordatorio(dto.getPreferenciaRecordatorio());
        return seguimiento;
    }

    public VisitaDomicilio toVisitaEntity(RegistrarVisitaRequestDTO dto, SeguimientoVisitas seguimiento) {
        VisitaDomicilio visita = new VisitaDomicilio();
        visita.setSeguimientoVisitas(seguimiento);
        visita.setFechaVisita(dto.getFechaVisita());
        visita.setContinuarVisitas(dto.getContinuarVisitas());

        EncuestaSeguimiento encuesta = new EncuestaSeguimiento();
        encuesta.setVisitaDomicilio(visita);
        encuesta.setEstadoGeneral(dto.getEstadoGeneral());
        encuesta.setLimpiezaLugar(dto.getLimpiezaLugar());
        encuesta.setAmbiente(dto.getAmbiente());

        visita.setEncuesta(encuesta);
        return visita;
    }

    public SeguimientoResponseDTO toResponseDTO(SeguimientoVisitas seguimiento) {
        return SeguimientoResponseDTO.builder()
                .id(seguimiento.getId())
                .adopcionId(seguimiento.getAdopcion().getId())
                .visitadorId(seguimiento.getVisitador().getId())
                .nombreVisitador(seguimiento.getVisitador().getNombre())
                .diaVisita(seguimiento.getDiaVisita())
                .horarioVisita(seguimiento.getHorarioVisita())
                .preferenciaRecordatorio(seguimiento.getPreferenciaRecordatorio())
                .cantidadVisitasRealizadas(seguimiento.getVisitas().size())
                .build();
    }

    public VisitaResponseDTO toVisitaResponseDTO(VisitaDomicilio visita) {
        VisitaResponseDTO.VisitaResponseDTOBuilder builder = VisitaResponseDTO.builder()
                .id(visita.getId())
                .seguimientoId(visita.getSeguimientoVisitas().getId())
                .fechaVisita(visita.getFechaVisita())
                .continuarVisitas(visita.getContinuarVisitas());

        if (visita.getEncuesta() != null) {
            builder.estadoGeneral(visita.getEncuesta().getEstadoGeneral());
            builder.limpiezaLugar(visita.getEncuesta().getLimpiezaLugar());
            builder.ambiente(visita.getEncuesta().getAmbiente());
        }
        return builder.build();
    }
}
