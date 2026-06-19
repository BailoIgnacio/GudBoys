package com.gudboys.service.impl;

import com.gudboys.dto.request.AtenderAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import com.gudboys.mapper.AlarmaMapper;
import com.gudboys.repository.IAlarmaRepository;
import com.gudboys.repository.IFichaMedicaRepository;
import com.gudboys.repository.IVeterinarioRepository;
import com.gudboys.service.IAtencionAlarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtencionAlarmaService implements IAtencionAlarmaService {

    private final IAlarmaRepository alarmaRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;
    private final IVeterinarioRepository veterinarioRepository;
    private final AlarmaMapper alarmaMapper;

    @Override
    public AlarmaResponseDTO atenderAlarma(Long alarmaId, AtenderAlarmaRequestDTO dto) {
        // TODO: implementar logica de atencion de alarma
        // Debe: crear RegistroAtencion, actualizar estado alarma, notificar si corresponde
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
