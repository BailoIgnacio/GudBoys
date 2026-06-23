package com.gudboys.controller;

import com.gudboys.domain.FichaMedica;
import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;
import com.gudboys.infrastructure.export.ExportadorFactory;
import com.gudboys.infrastructure.export.IExportadorStrategy;
import com.gudboys.repository.IFichaMedicaRepository;
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
    private final ExportadorFactory exportadorFactory;
    private final IFichaMedicaRepository fichaMedicaRepository;

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

    @GetMapping("/{id}/ficha-medica/exportar")
    public ResponseEntity<String> exportarFichaMedica(@PathVariable Long id,
                                                        @RequestParam String formato,
                                                        @RequestParam(defaultValue = "false") boolean encriptar,
                                                        @RequestParam(defaultValue = "false") boolean agua) {

        // Se consigue la ficha medica del animal
        FichaMedica fichaMedica = fichaMedicaRepository.findByAnimalId(id)
                .orElseThrow(() -> new RuntimeException("Ficha medica no encontrada con id: " + id));

        // Se busca la clase a la que responde. Osea, si es formato excel, exportador es exportadorExcel, sino es ExportadorPDF.
        IExportadorStrategy exportador = exportadorFactory.crearExportador(formato, encriptar, agua);

        // Se llama al metodo exportar y se le pasa la fichaMedica. Aca exportador ya es o ExportadorPDF o Excel, pero como ambos comparten mismo metodo, simplemente se llama a exportar
        return ResponseEntity.ok(exportador.exportar(fichaMedica));



    }
}
