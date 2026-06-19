package com.gudboys.service.impl;

import com.gudboys.domain.Animal;
import com.gudboys.domain.FichaMedica;
import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;
import com.gudboys.exceptions.animal.AnimalException;
import com.gudboys.exceptions.animal.AnimalNotFoundException;
import com.gudboys.mapper.AnimalMapper;
import com.gudboys.repository.IAnimalRepository;
import com.gudboys.repository.IFichaMedicaRepository;
import com.gudboys.service.IIngresoAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngresoAnimalService implements IIngresoAnimalService {

    private final IAnimalRepository animalRepository;
    private final IFichaMedicaRepository fichaMedicaRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalResponseDTO ingresarAnimal(IngresarAnimalRequestDTO dto) {

        //valido el animal
        if (!dto.getTipoAnimal().equalsIgnoreCase("DOMESTICO") && !dto.getTipoAnimal().equalsIgnoreCase("SALVAJE")) {
            throw new AnimalException("Tipo de animal no valido"); // Excepcion especifica creada :) 
        } 

        Animal animalEntity = animalMapper.toEntity(dto);

        //Relaciono al animal nuevo con una ficha medica vacía y guardo ambas
        FichaMedica ficha = new FichaMedica();
        ficha.setAnimal(animalEntity);
        animalEntity.setFichaMedica(ficha);

        //Persisto el animal nuevo en la db, la ficha persiste por la relación tipo cascade
        animalRepository.save(animalEntity);

        return animalMapper.toResponseDTO(animalEntity);
    }

    @Override
    public AnimalResponseDTO obtenerAnimal(Long id) {
        
        // Valido el animal con el método private de abajo
        Animal animal = validarAnimal(id);
            
        return animalMapper.toResponseDTO(animal);
    }

    @Override
    public List<AnimalResponseDTO> listarAnimales() {
        
        List<Animal> animales = animalRepository.findAll();
        if (animales.isEmpty()) throw new AnimalNotFoundException("Animal no encontrado"); 

        return animales.stream()
            .map(animalMapper::toResponseDTO)
            .toList();
    }

    @Override
    public FichaMedicaResponseDTO obtenerFichaMedica(Long animalId) {

        //valido que exista el animal por las dudas
        Animal animal = validarAnimal(animalId);

        //Valido qeu exista la ficha
        FichaMedica ficha = fichaMedicaRepository.findByAnimalId(animalId)
        .orElseThrow(() -> new AnimalNotFoundException(animalId));

        return animalMapper.toFichaMedicaResponseDTO(ficha);

    }

    private Animal validarAnimal(Long id){
        //valido que exista el animal por las dudas
        Animal animal = animalRepository.findById(id)
            .orElseThrow(()-> new AnimalNotFoundException(id));
        return animal;
        }
}
