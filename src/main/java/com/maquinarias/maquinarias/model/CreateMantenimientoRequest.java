package com.maquinarias.maquinarias.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateMantenimientoRequest {
    private UUID machineryId;
    private String maintenanceType;
    private LocalDateTime date;
    private String technician;
    private String description;
    @JsonProperty("cost")
    private Double cost;
}