package com.gudboys.controller;

import com.gudboys.dto.request.ConfigurarSeguimientoRequestDTO;
import com.gudboys.dto.request.RegistrarVisitaRequestDTO;
import com.gudboys.dto.response.SeguimientoResponseDTO;
import com.gudboys.dto.response.VisitaResponseDTO;
import com.gudboys.service.IVisitaSeguimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
@RequiredArgsConstructor
public class SeguimientoController {

    private final IVisitaSeguimientoService visitaSeguimientoService;

    @PostMapping
    public ResponseEntity<SeguimientoResponseDTO> configurarSeguimiento(@Valid @RequestBody ConfigurarSeguimientoRequestDTO dto) {
        SeguimientoResponseDTO response = visitaSeguimientoService.configurarSeguimiento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguimientoResponseDTO> obtenerSeguimiento(@PathVariable Long id) {
        return ResponseEntity.ok(visitaSeguimientoService.obtenerSeguimiento(id));
    }

    @PostMapping("/{id}/visitas")
    public ResponseEntity<VisitaResponseDTO> registrarVisita(
            @PathVariable Long id,
            @Valid @RequestBody RegistrarVisitaRequestDTO dto) {
        VisitaResponseDTO response = visitaSeguimientoService.registrarVisita(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/visitas")
    public ResponseEntity<List<VisitaResponseDTO>> listarVisitas(@PathVariable Long id) {
        return ResponseEntity.ok(visitaSeguimientoService.listarVisitas(id));
    }
}
