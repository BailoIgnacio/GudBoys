package com.gudboys.service;

import com.gudboys.dto.request.ConfigurarSeguimientoRequestDTO;
import com.gudboys.dto.request.RegistrarVisitaRequestDTO;
import com.gudboys.dto.response.SeguimientoResponseDTO;
import com.gudboys.dto.response.VisitaResponseDTO;

import java.util.List;

public interface IVisitaSeguimientoService {

    SeguimientoResponseDTO configurarSeguimiento(ConfigurarSeguimientoRequestDTO dto);

    SeguimientoResponseDTO obtenerSeguimiento(Long id);

    VisitaResponseDTO registrarVisita(Long seguimientoId, RegistrarVisitaRequestDTO dto);

    List<VisitaResponseDTO> listarVisitas(Long seguimientoId);

    void enviarRecordatoriosProximos();
}
