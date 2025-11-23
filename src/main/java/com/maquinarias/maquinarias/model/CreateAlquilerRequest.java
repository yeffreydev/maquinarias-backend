package com.maquinarias.maquinarias.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateAlquilerRequest {
    private UUID machineryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double cost;
    private String status;
}