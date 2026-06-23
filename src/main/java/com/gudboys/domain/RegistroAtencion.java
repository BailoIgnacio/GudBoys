package com.gudboys.domain;

import com.gudboys.domain.accion.AccionComposite;
import com.gudboys.domain.accion.AccionFactory;
import com.gudboys.domain.enums.AccionAlarma;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registros_atencion")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class RegistroAtencion extends Evento {

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private Boolean tratamientoFinalizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private Veterinario veterinario;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "registro_acciones", joinColumns = @JoinColumn(name = "registro_id"))
    @Column(name = "accion")
    private List<AccionAlarma> accionesRealizadas = new ArrayList<>();

    /** Vista Composite de las acciones realizadas (enum persistido → árbol en memoria). */
    @Transient
    public AccionComposite getAccionesRealizadasComposite() {
        return AccionFactory.crearComposite(accionesRealizadas);
    }
}
