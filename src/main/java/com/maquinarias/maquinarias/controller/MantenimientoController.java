package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.CreateMantenimientoRequest;
import com.maquinarias.maquinarias.model.Mantenimiento;
import com.maquinarias.maquinarias.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @GetMapping
    public ResponseEntity<List<Mantenimiento>> getAllMantenimientos() {
        List<Mantenimiento> mantenimientos = mantenimientoService.getAllMantenimientos();
        return ResponseEntity.ok(mantenimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mantenimiento> getMantenimientoById(@PathVariable UUID id) {
        Optional<Mantenimiento> mantenimiento = mantenimientoService.getMantenimientoById(id);
        return mantenimiento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mantenimiento> createMantenimiento(@RequestBody CreateMantenimientoRequest request) {
        Mantenimiento createdMantenimiento = mantenimientoService.createMantenimiento(request);
        return ResponseEntity.ok(createdMantenimiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mantenimiento> updateMantenimiento(@PathVariable UUID id, @RequestBody Mantenimiento mantenimientoDetails) {
        Mantenimiento updatedMantenimiento = mantenimientoService.updateMantenimiento(id, mantenimientoDetails);
        if (updatedMantenimiento != null) {
            return ResponseEntity.ok(updatedMantenimiento);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMantenimiento(@PathVariable UUID id) {
        boolean deleted = mantenimientoService.deleteMantenimiento(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}