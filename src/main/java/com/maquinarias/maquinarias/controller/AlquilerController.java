package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.Alquiler;
import com.maquinarias.maquinarias.model.CreateAlquilerRequest;
import com.maquinarias.maquinarias.service.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAllAlquileres() {
        List<Alquiler> alquileres = alquilerService.getAllAlquileres();
        return ResponseEntity.ok(alquileres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alquiler> getAlquilerById(@PathVariable UUID id) {
        Optional<Alquiler> alquiler = alquilerService.getAlquilerById(id);
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alquiler> createAlquiler(@RequestBody CreateAlquilerRequest request) {
        Alquiler createdAlquiler = alquilerService.createAlquiler(request);
        return ResponseEntity.ok(createdAlquiler);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alquiler> updateAlquiler(@PathVariable UUID id, @RequestBody Alquiler alquilerDetails) {
        Alquiler updatedAlquiler = alquilerService.updateAlquiler(id, alquilerDetails);
        if (updatedAlquiler != null) {
            return ResponseEntity.ok(updatedAlquiler);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlquiler(@PathVariable UUID id) {
        boolean deleted = alquilerService.deleteAlquiler(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}