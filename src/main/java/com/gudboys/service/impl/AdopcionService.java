package com.gudboys.service.impl;

import com.gudboys.domain.Adopcion;
import com.gudboys.domain.Adoptante;
import com.gudboys.domain.Animal;
import com.gudboys.domain.AnimalDomestico;
import com.gudboys.dto.request.RegistrarAdopcionRequestDTO;
import com.gudboys.dto.response.AdopcionResponseDTO;
import com.gudboys.mapper.AdopcionMapper;
import com.gudboys.repository.IAdopcionRepository;
import com.gudboys.repository.IAdoptanteRepository;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.service.IAdopcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdopcionService implements IAdopcionService {

    private final IAdopcionRepository adopcionRepository;
    private final IAdoptanteRepository adoptanteRepository;
    private final IAnimalRepository animalRepository;
    private final AdopcionMapper adopcionMapper;

    @Override
    public AdopcionResponseDTO registrarAdopcion(RegistrarAdopcionRequestDTO dto) {
        // Debe validar: animal adoptable (RN-07, RN-08), limite 2 adopciones por adoptante (RN-09)

        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id: " + dto.getAnimalId()));

        // Se chequea que el animal existea y no sea salvaje (esAdoptable) y que no este bajo tratamiento medico
        if(!animal.esAdoptable() || animal.getFichaMedica().estaBajoTratamientoActivo()){
            throw new RuntimeException("El animal no esta disponible para adoptar");
        }

        // Se mapea el adoptante
        Adoptante adoptante = adopcionMapper.toAdoptanteEntity(dto);

        // Se cheque que el adoptante no haya adoptado mas de 2 animales
        if (adoptante.getCantAnimalesAdoptados() >= 2){
            throw new RuntimeException("El adopante ya adopto mas de 2 animales");
        }

        // Se incrementa el numero de adoptas y se guarda adoptante en su repositorioW
        adoptante.setCantAnimalesAdoptados(adoptante.getCantAnimalesAdoptados() + 1);
        Adoptante adop = adoptanteRepository.save(adoptante);

        // Se convierte el animal en tipo AnimalDomestico
        AnimalDomestico animalDomestico = (AnimalDomestico) animal;

        // Se mapea a entidad y se guarda y se guarda
        Adopcion save = adopcionMapper.toAdopcionEntity(adop, animalDomestico);
        adopcionRepository.save(save);

        // Se mapea a response
        AdopcionResponseDTO adopcionResponseDTO = adopcionMapper.toResponseDTO(save);

        return adopcionResponseDTO;
    }

    @Override
    public AdopcionResponseDTO obtenerAdopcion(Long id) {
        // TODO: implementar logica de busqueda de adopcion

        Adopcion adopcion = adopcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adopcion no encontrada con id: " + id));

        return adopcionMapper.toResponseDTO(adopcion);
    }

    @Override
    public List<AdopcionResponseDTO> listarAdopciones() {
        // TODO: implementar logica de listado de adopciones

        // Creo 2 listas, una con todos las adopciones y la otra vacia donde van a ir todas las adopcionesResponse
        List<Adopcion> adopcionList = adopcionRepository.findAll();
        List<AdopcionResponseDTO> adopcionResponseDTOS = new ArrayList<>();

        // Por cada adopcion, se convierte en response y se guarda en la lista
        for (Adopcion adopcion: adopcionList){
            adopcionResponseDTOS.add(adopcionMapper.toResponseDTO(adopcion));
        }

        return adopcionResponseDTOS;
    }
}
