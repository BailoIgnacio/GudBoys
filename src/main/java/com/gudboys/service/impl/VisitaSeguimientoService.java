package com.gudboys.service.impl;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitaSeguimientoService implements IVisitaSeguimientoService {

    private final ISeguimientoVisitasRepository seguimientoRepository;
    private final IAdopcionRepository adopcionRepository;
    private final IVisitadorRepository visitadorRepository;
    private final SeguimientoMapper seguimientoMapper;

    @Override
    public SeguimientoResponseDTO configurarSeguimiento(ConfigurarSeguimientoRequestDTO dto) {
        // TODO: implementar logica de configuracion de seguimiento
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SeguimientoResponseDTO obtenerSeguimiento(Long id) {
        // TODO: implementar logica de obtencion de seguimiento
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public VisitaResponseDTO registrarVisita(Long seguimientoId, RegistrarVisitaRequestDTO dto) {
        // TODO: implementar logica de registro de visita y encuesta
        // Debe agregar VisitaDomicilio a la ficha medica del animal (RN-14)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<VisitaResponseDTO> listarVisitas(Long seguimientoId) {
        // TODO: implementar logica de listado de visitas
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void enviarRecordatoriosProximos() {
        // TODO: implementar logica de envio de recordatorios (usar IRecordatorioStrategy)
        // Buscar visitas en los proximos N dias (gudboys.recordatorio.dias-anticipacion)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
