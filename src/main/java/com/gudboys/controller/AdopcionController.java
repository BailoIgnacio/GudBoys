package com.gudboys.controller;

import com.gudboys.dto.request.RegistrarAdopcionRequestDTO;
import com.gudboys.dto.response.AdopcionResponseDTO;
import com.gudboys.service.IAdopcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adopciones")
@RequiredArgsConstructor
public class AdopcionController {

    private final IAdopcionService adopcionService;

    @PostMapping
    public ResponseEntity<AdopcionResponseDTO> registrarAdopcion(@Valid @RequestBody RegistrarAdopcionRequestDTO dto) {
        AdopcionResponseDTO response = adopcionService.registrarAdopcion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdopcionResponseDTO>> listarAdopciones() {
        return ResponseEntity.ok(adopcionService.listarAdopciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopcionResponseDTO> obtenerAdopcion(@PathVariable Long id) {
        return ResponseEntity.ok(adopcionService.obtenerAdopcion(id));
    }
}
