package com.gudboys.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fichas_medicas")
@Getter
@Setter
@NoArgsConstructor
public class FichaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false, unique = true)
    private Animal animal;

    @OneToMany(mappedBy = "fichaMedica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("fechaHora ASC")
    private List<Evento> eventos = new ArrayList<>();

    public void agregarEvento(Evento evento) {
        evento.setFichaMedica(this);
        eventos.add(evento);
    }

    public boolean estaBajoTratamientoActivo() {
        return animal.getAlarmas().stream()
                .anyMatch(a -> a.isEsTratamientoMedico()
                        && (a.getEstado().name().equals("ACTIVA") || a.getEstado().name().equals("ATENDIDA")));
    }
}
