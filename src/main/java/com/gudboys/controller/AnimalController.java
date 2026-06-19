package com.gudboys.controller;

import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;
import com.gudboys.service.IIngresoAnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animales")
@RequiredArgsConstructor
public class AnimalController {

    private final IIngresoAnimalService ingresoAnimalService;

    @PostMapping
    public ResponseEntity<AnimalResponseDTO> ingresarAnimal(@Valid @RequestBody IngresarAnimalRequestDTO dto) {
        AnimalResponseDTO response = ingresoAnimalService.ingresarAnimal(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> listarAnimales() {
        return ResponseEntity.ok(ingresoAnimalService.listarAnimales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> obtenerAnimal(@PathVariable Long id) {
        return ResponseEntity.ok(ingresoAnimalService.obtenerAnimal(id));
    }

    @GetMapping("/{id}/ficha-medica")
    public ResponseEntity<FichaMedicaResponseDTO> obtenerFichaMedica(@PathVariable Long id) {
        return ResponseEntity.ok(ingresoAnimalService.obtenerFichaMedica(id));
    }
}
