package com.gudboys.mapper;

import com.gudboys.domain.Adopcion;
import com.gudboys.domain.Adoptante;
import com.gudboys.domain.AnimalDomestico;
import com.gudboys.dto.request.RegistrarAdopcionRequestDTO;
import com.gudboys.dto.response.AdopcionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AdopcionMapper {

    public Adoptante toAdoptanteEntity(RegistrarAdopcionRequestDTO dto) {
        Adoptante adoptante = new Adoptante();
        adoptante.setNombre(dto.getNombre());
        adoptante.setApellido(dto.getApellido());
        adoptante.setEstadoCivil(dto.getEstadoCivil());
        adoptante.setEmail(dto.getEmail());
        adoptante.setTelefono(dto.getTelefono());
        adoptante.setOcupacion(dto.getOcupacion());
        adoptante.setTieneMascotas(dto.getTieneMascotas());
        adoptante.setMotivoAdopcion(dto.getMotivoAdopcion());
        if (dto.getTiposAnimalesInteresados() != null) {
            adoptante.setTiposAnimalesInteresados(dto.getTiposAnimalesInteresados());
        }
        return adoptante;
    }

    public Adopcion toAdopcionEntity(Adoptante adoptante, AnimalDomestico animal) {
        Adopcion adopcion = new Adopcion();
        adopcion.setAdoptante(adoptante);
        adopcion.setAnimal(animal);
        return adopcion;
    }

    public AdopcionResponseDTO toResponseDTO(Adopcion adopcion) {
        return AdopcionResponseDTO.builder()
                .id(adopcion.getId())
                .animalId(adopcion.getAnimal().getId())
                .especieAnimal(adopcion.getAnimal().getEspecie())
                .adoptanteId(adopcion.getAdoptante().getId())
                .nombreAdoptante(adopcion.getAdoptante().getNombre())
                .apellidoAdoptante(adopcion.getAdoptante().getApellido())
                .fechaAdopcion(adopcion.getFechaAdopcion())
                .seguimientoId(adopcion.getSeguimientoVisitas() != null ? adopcion.getSeguimientoVisitas().getId() : null)
                .build();
    }
}
