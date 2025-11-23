package com.maquinarias.maquinarias.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mantenimientos")
@Data
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "maquina_id", nullable = false)
    @JsonIgnore
    private Maquina maquina;

    @JsonProperty("machineryId")
    public UUID getMachineryId() {
        return maquina != null ? maquina.getId() : null;
    }

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double costo;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String tecnico;
}