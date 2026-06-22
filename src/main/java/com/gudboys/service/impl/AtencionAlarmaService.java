package com.gudboys.service.impl;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.RegistroAtencion;
import com.gudboys.domain.Veterinario;
import com.gudboys.dto.request.AtenderAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import com.gudboys.mapper.AlarmaMapper;
import com.gudboys.repository.IAlarmaRepository;
import com.gudboys.repository.IFichaMedicaRepository;
import com.gudboys.repository.IVeterinarioRepository;
import com.gudboys.service.IAtencionAlarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gudboys.domain.FichaMedica;

@Service
@RequiredArgsConstructor
public class AtencionAlarmaService implements IAtencionAlarmaService {

    private final IAlarmaRepository alarmaRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;
    private final IVeterinarioRepository veterinarioRepository;
    private final AlarmaMapper alarmaMapper;

    @Override
    public AlarmaResponseDTO atenderAlarma(Long alarmaId, AtenderAlarmaRequestDTO dto) {
        //TODO: FALTA HACER BIEN LAS EXCEPCIONESSS
        
        //valido alarma
        Alarma alarma = alarmaRepository.findById(alarmaId)
            .orElseThrow(()-> new RuntimeException("No existe esta alarma"));

        //Valido veterinario
        Veterinario veterinario= veterinarioRepository.findById(dto.getVeterinarioId())
            .orElseThrow(()-> new RuntimeException("Veterinario no encontrado"));

        FichaMedica ficha= fichaMedicaRepository.findByAnimalId(alarma.getAnimal().getId())
            .orElseThrow(()-> new RuntimeException("Este animal no tiene ficha"));
            
        //Creo y seteo el registro
        RegistroAtencion registro = new RegistroAtencion();
        registro.setVeterinario(veterinario);
        registro.setAccionesRealizadas(dto.getAccionesRealizadas());
        registro.setComentario(dto.getComentario());
        registro.setTratamientoFinalizado(dto.getTratamientoFinalizado());
        registro.setDescripcion("Atención de alarma: " + dto.getAccionesRealizadas());

        //persiste por cascade
        ficha.agregarEvento(registro);
        
        //Cambio estado de la alarma
        alarma.getEstado().atender(alarma, Boolean.TRUE.equals(dto.getTratamientoFinalizado()));
        
        // Guardo las entidades que modifiqué
        fichaMedicaRepository.save(ficha);
        alarmaRepository.save(alarma);

        //devuelvo el dto
        return alarmaMapper.toResponseDTO(alarma);
    }
}
