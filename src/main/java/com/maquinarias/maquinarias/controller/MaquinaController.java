package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.service.MaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/maquinas")
public class MaquinaController {

    @Autowired
    private MaquinaService maquinaService;

    @GetMapping
    public ResponseEntity<List<Maquina>> getAllMaquinas() {
        List<Maquina> maquinas = maquinaService.getAllMaquinas();
        return ResponseEntity.ok(maquinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maquina> getMaquinaById(@PathVariable UUID id) {
        Optional<Maquina> maquina = maquinaService.getMaquinaById(id);
        return maquina.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Maquina> createMaquina(@RequestBody Maquina maquina) {
        Maquina createdMaquina = maquinaService.createMaquina(maquina);
        return ResponseEntity.ok(createdMaquina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maquina> updateMaquina(@PathVariable UUID id, @RequestBody Maquina maquinaDetails) {
        Maquina updatedMaquina = maquinaService.updateMaquina(id, maquinaDetails);
        if (updatedMaquina != null) {
            return ResponseEntity.ok(updatedMaquina);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaquina(@PathVariable UUID id) {
        boolean deleted = maquinaService.deleteMaquina(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}