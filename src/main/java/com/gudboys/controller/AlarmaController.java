package com.gudboys.controller;

import com.gudboys.dto.request.AtenderAlarmaRequestDTO;
import com.gudboys.dto.request.CrearAlarmaRequestDTO;
import com.gudboys.dto.response.AlarmaResponseDTO;
import com.gudboys.service.IAtencionAlarmaService;
import com.gudboys.service.IGestionAlarmaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmaController {

    private final IGestionAlarmaService gestionAlarmaService;
    private final IAtencionAlarmaService atencionAlarmaService;

    @PostMapping("/api/animales/{animalId}/alarmas")
    public ResponseEntity<AlarmaResponseDTO> crearAlarma(
            @PathVariable Long animalId,
            @Valid @RequestBody CrearAlarmaRequestDTO dto) {
        AlarmaResponseDTO response = gestionAlarmaService.crearAlarma(animalId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/animales/{animalId}/alarmas")
    public ResponseEntity<List<AlarmaResponseDTO>> listarAlarmasPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(gestionAlarmaService.listarAlarmasPorAnimal(animalId));
    }

    @PutMapping("/api/alarmas/{id}")
    public ResponseEntity<AlarmaResponseDTO> actualizarAlarma(
            @PathVariable Long id,
            @Valid @RequestBody CrearAlarmaRequestDTO dto) {
        return ResponseEntity.ok(gestionAlarmaService.actualizarAlarma(id, dto));
    }

    @PostMapping("/api/alarmas/{id}/atender")
    public ResponseEntity<AlarmaResponseDTO> atenderAlarma(
            @PathVariable Long id,
            @Valid @RequestBody AtenderAlarmaRequestDTO dto) {
        return ResponseEntity.ok(atencionAlarmaService.atenderAlarma(id, dto));
    }
}
