package com.gudboys.domain;

import com.gudboys.domain.enums.Ocupacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adoptantes")
@Getter
@Setter
@NoArgsConstructor
public class Adoptante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    private String estadoCivil;

    @Column(nullable = false)
    private String email;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ocupacion ocupacion;

    @Column(nullable = false)
    private Boolean tieneMascotas;

    @Column(columnDefinition = "TEXT")
    private String motivoAdopcion;

    @ElementCollection
    @CollectionTable(name = "adoptante_tipos_animales", joinColumns = @JoinColumn(name = "adoptante_id"))
    @Column(name = "tipo_animal")
    private List<String> tiposAnimalesInteresados = new ArrayList<>();

    @Column(nullable = false)
    private Integer cantAnimalesAdoptados = 0;

    @OneToMany(mappedBy = "adoptante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Adopcion> adopciones = new ArrayList<>();
}
