package com.maquinarias.maquinarias.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "maquinas")
@Data
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Double tarifaHora;

    @Column(nullable = true)
    private String imagen;

    @Column(nullable = true)
    private String potencia;

    @Column(nullable = true)
    private String capacidad;

    @Column(nullable = true)
    private String peso;

    @Column(nullable = true)
    private String dimensiones;

    @OneToMany(mappedBy = "maquina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Alquiler> alquileres;

    @OneToMany(mappedBy = "maquina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mantenimiento> mantenimientos;
}