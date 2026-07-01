package com.gudboys.mapper;

import com.gudboys.domain.Animal;
import com.gudboys.domain.AnimalDomestico;
import com.gudboys.domain.AnimalSalvaje;
import com.gudboys.domain.Evento;
import com.gudboys.domain.RegistroAtencion;
import com.gudboys.dto.request.IngresarAnimalRequestDTO;
import com.gudboys.dto.response.AnimalResponseDTO;
import com.gudboys.dto.response.EventoResponseDTO;
import com.gudboys.dto.response.FichaMedicaResponseDTO;
import com.gudboys.repository.IFichaMedicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnimalMapper {

    private final IFichaMedicaRepository fichaMedicaRepository;

    public Animal toEntity(IngresarAnimalRequestDTO dto) {
        Animal animal;
        if ("DOMESTICO".equalsIgnoreCase(dto.getTipoAnimal())) {
            AnimalDomestico domestico = new AnimalDomestico();
            domestico.setEspecie(dto.getEspecie());
            animal = domestico;
        } else {
            AnimalSalvaje salvaje = new AnimalSalvaje();
            salvaje.setEspecie(dto.getEspecie());
            animal = salvaje;
        }
        animal.setAltura(dto.getAltura());
        animal.setPeso(dto.getPeso());
        animal.setEdad(dto.getEdad());
        animal.setCondicionMedica(dto.getCondicionMedica());
        return animal;
    }

    public AnimalResponseDTO toResponseDTO(Animal animal) {
        String especie = animal instanceof AnimalDomestico
                ? ((AnimalDomestico) animal).getEspecie()
                : ((AnimalSalvaje) animal).getEspecie();

        String tipoAnimal = animal instanceof AnimalDomestico ? "DOMESTICO" : "SALVAJE";

        return AnimalResponseDTO.builder()
                .id(animal.getId())
                .tipoAnimal(tipoAnimal)
                .especie(especie)
                .altura(animal.getAltura())
                .peso(animal.getPeso())
                .edad(animal.getEdad())
                .condicionMedica(animal.getCondicionMedica())
                .esAdoptable(animal.esAdoptable())
                .fichaMedicaId(fichaMedicaRepository.findByAnimalId(animal.getId()).map(f -> f.getId()).orElse(null))
                .build();
    }

    public FichaMedicaResponseDTO toFichaMedicaResponseDTO(com.gudboys.domain.FichaMedica ficha) {
        List<EventoResponseDTO> eventos = ficha.getEventos().stream()
                .map(this::toEventoResponseDTO)
                .collect(Collectors.toList());

        String especie = ficha.getAnimal() instanceof AnimalDomestico
                ? ((AnimalDomestico) ficha.getAnimal()).getEspecie()
                : ((AnimalSalvaje) ficha.getAnimal()).getEspecie();

        return FichaMedicaResponseDTO.builder()
                .id(ficha.getId())
                .animalId(ficha.getAnimal().getId())
                .especieAnimal(especie)
                .bajoTratamientoActivo(ficha.estaBajoTratamientoActivo())
                .eventos(eventos)
                .build();
    }

    private EventoResponseDTO toEventoResponseDTO(Evento evento) {
        EventoResponseDTO.EventoResponseDTOBuilder builder = EventoResponseDTO.builder()
                .id(evento.getId())
                .tipo(evento.getClass().getSimpleName())
                .descripcion(evento.getDescripcion())
                .fechaHora(evento.getFechaHora());

        if (evento instanceof RegistroAtencion registro) {
            builder.comentario(registro.getComentario());
            builder.tratamientoFinalizado(registro.getTratamientoFinalizado());
            if (registro.getVeterinario() != null) {
                builder.nombreVeterinario(registro.getVeterinario().getNombre());
            }
        }
        return builder.build();
    }
}
