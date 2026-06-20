package com.gudboys.service.impl;

import com.gudboys.domain.Adopcion;
import com.gudboys.domain.FichaMedica;
import com.gudboys.domain.SeguimientoVisitas;
import com.gudboys.domain.VisitaDomicilio;
import com.gudboys.domain.Visitador;
import com.gudboys.dto.request.ConfigurarSeguimientoRequestDTO;
import com.gudboys.dto.request.RegistrarVisitaRequestDTO;
import com.gudboys.dto.response.SeguimientoResponseDTO;
import com.gudboys.dto.response.VisitaResponseDTO;
import com.gudboys.mapper.SeguimientoMapper;
import com.gudboys.repository.IAdopcionRepository;
import com.gudboys.repository.ISeguimientoVisitasRepository;
import com.gudboys.repository.IVisitadorRepository;
import com.gudboys.service.IVisitaSeguimientoService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VisitaSeguimientoService implements IVisitaSeguimientoService {

    private final ISeguimientoVisitasRepository seguimientoRepository;
    private final IAdopcionRepository adopcionRepository;
    private final IVisitadorRepository visitadorRepository;
    private final SeguimientoMapper seguimientoMapper;

    //-------------------------------------------------------------------------------

    @Transactional
    @Override
    public SeguimientoResponseDTO configurarSeguimiento(ConfigurarSeguimientoRequestDTO dto) {
        //valido primero la Adopcion y Visitador
        Visitador visitador = visitadorRepository.findById(dto.getVisitadorId())
            .orElseThrow(()-> new RuntimeException("Visitador no registrado"));
        
        Adopcion adopcion= adopcionRepository.findById(dto.getAdopcionId())
            .orElseThrow(()-> new RuntimeException("Adopcion no encontrada"));

        //Solo puedo cargar un seguimiento por adopcion, así que valido que no exista un seguimeinto para esa adopcion
        Optional<SeguimientoVisitas> seguimientoVerficar = seguimientoRepository.findByAdopcionId(dto.getAdopcionId());
        if (seguimientoVerficar.isPresent()) throw new RuntimeException("Ya exsiste seguimiento para esa adopcion");
           
        //mapeo a entidad    
        SeguimientoVisitas seguimiento = seguimientoMapper.toEntity(dto, adopcion, visitador);

        //guardo en el repo el seguimiento
        seguimientoRepository.save(seguimiento);

        return seguimientoMapper.toResponseDTO(seguimiento);
    }

    //-------------------------------------------------------------------------------

    //pongo el transactional xq como carga el resto de resto de entidades (visitador, adopcion y visitas) puede tirar err por ser lazy
    @Transactional(readOnly = true)
    @Override
    public SeguimientoResponseDTO obtenerSeguimiento(Long id) {
        // Valido que exista el seguimiento o tiro error (dsp vemos de personalizar todos los tipos de errores)
        SeguimientoVisitas seg = seguimientoRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("No existe seguimiento con ese ID"));

        return seguimientoMapper.toResponseDTO(seg);
    }

    //-------------------------------------------------------------------------------

    //Aca hay un monton de cosas que se guardan por Cascade, documentar bien y falta algo de notificar o recordatorio que todavia no está implementado
    @Transactional
    @Override
    public VisitaResponseDTO registrarVisita(Long seguimientoId, RegistrarVisitaRequestDTO dto) {
        //valido seguimiento
        SeguimientoVisitas seguimiento= seguimientoRepository.findById(seguimientoId)
                .orElseThrow(()-> new RuntimeException("El seguimiento con ese ID no existe"));
        
        VisitaDomicilio visita = seguimientoMapper.toVisitaEntity(dto, seguimiento);

        //guardo la visita en la Lista de visitas que tiene un seguimiento y actualizo el seguimiento
        seguimiento.getVisitas().add(visita);
        seguimientoRepository.save(seguimiento);

        return seguimientoMapper.toVisitaResponseDTO(visita);
    }

    //-------------------------------------------------------------------------------

    @Transactional(readOnly=true)
    @Override
    public List<VisitaResponseDTO> listarVisitas(Long seguimientoId) {
        //valido seguimiento
        SeguimientoVisitas seg = seguimientoRepository.findById(seguimientoId)
            .orElseThrow(()-> new RuntimeException("Seguimiento no encontrado"));
        
        //salvo la lista de visitas
        List<VisitaDomicilio> visitas = seg.getVisitas();

        //uso stream para mappear cada visitaDomiciolo a VisitaResponseDTO
        return visitas.stream()
                .map(seguimientoMapper::toVisitaResponseDTO)
                .toList();
    }

    //-------------------------------------------------------------------------------

    //Para este hay que solucionar los senders primero.
    @Override
    public void enviarRecordatoriosProximos() {

        // TODO: implementar logica de envio de recordatorios (usar IRecordatorioStrategy)
        // Buscar visitas en los proximos N dias (gudboys.recordatorio.dias-anticipacion)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
