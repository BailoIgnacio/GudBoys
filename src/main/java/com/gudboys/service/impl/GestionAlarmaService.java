package com.gudboys.service.impl;

import com.gudboys.domain.Alarma;
import com.gudboys.domain.Animal;
import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import com.gudboys.mapper.AlarmaMapper;
import com.gudboys.repository.IAlarmaRepository;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.service.IGestionAlarmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionAlarmaService implements IGestionAlarmaService {

    private final IAlarmaRepository alarmaRepository;
    private final IAnimalRepository animalRepository;
    private final AlarmaMapper alarmaMapper;

    @Override
    public AlarmaResponseDTO crearAlarma(Long animalId, CrearAlarmaRequestDTO dto) {
        // TODO: implementar logica de creacion de alarma

        // Se busca el animal a traves de su id
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id: " + animalId));

        // Se convierte a entidad y se guarda
        Alarma save = alarmaMapper.toEntity(dto, animal);
        save = alarmaRepository.save(save);

        // Se convierte a response y se retorna
        AlarmaResponseDTO alarmaResponseDTO = alarmaMapper.toResponseDTO(save);
        return alarmaResponseDTO;

    }

    @Override
    @Transactional
    public AlarmaResponseDTO actualizarAlarma(Long alarmaId, CrearAlarmaRequestDTO dto) {
        // TODO: implementar logica de actualizacion de alarma

        // Se encuentra la alarma que se desea actualizar
        Alarma alarma = alarmaRepository.findById(alarmaId)
                .orElseThrow(() -> new RuntimeException("Alarma no encontrado con id: " + alarmaId));

        // Se actualiza la alarma con sus nuevos datos y se guarda (No se duplica en el repo xq Spring JPA detecta mediante el id que ya existe y hace un update)
        Alarma alarmaActualizada = alarmaMapper.actualizarEntity(dto, alarma);
        alarmaActualizada = alarmaRepository.save(alarmaActualizada);

        // Se convierte a response y se retorna
        AlarmaResponseDTO alarmaResponseDTO = alarmaMapper.toResponseDTO(alarmaActualizada);
        return alarmaResponseDTO;
    }

    @Override
    public List<AlarmaResponseDTO> listarAlarmasPorAnimal(Long animalId) {
        // TODO: implementar logica de listado de alarmas por animal

        // Se busca todas las alarmas de ese animal y se crea una lista de alarmaResponse vacia
        List<Alarma> alarmas = alarmaRepository.findByAnimalId(animalId);
        List<AlarmaResponseDTO> alarmaResponseDTOS = new ArrayList<>();

        // Se recorre la lista de alarmas del animal, se convierte a response y se agrega a la lista de alarmasResponse
        for (Alarma alarma : alarmas) {
            alarmaResponseDTOS.add(alarmaMapper.toResponseDTO(alarma));
        }

        // Se retorna la lista de alarmasResponse
        return alarmaResponseDTOS;
    }
}
