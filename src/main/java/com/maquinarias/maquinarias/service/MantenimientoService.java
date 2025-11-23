package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.CreateMantenimientoRequest;
import com.maquinarias.maquinarias.model.Mantenimiento;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.repository.MantenimientoRepository;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private MaquinaRepository maquinaRepository;

    public List<Mantenimiento> getAllMantenimientos() {
        return mantenimientoRepository.findAll();
    }

    public Optional<Mantenimiento> getMantenimientoById(UUID id) {
        return mantenimientoRepository.findById(id);
    }

    public Mantenimiento createMantenimiento(CreateMantenimientoRequest request) {
        Maquina maquina = maquinaRepository.findById(request.getMachineryId())
                .orElseThrow(() -> new RuntimeException("Maquina not found"));

        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setMaquina(maquina);
        mantenimiento.setFecha(request.getDate());
        mantenimiento.setTipo(request.getMaintenanceType());
        mantenimiento.setTecnico(request.getTechnician());
        mantenimiento.setDescripcion(request.getDescription());
        mantenimiento.setCosto(request.getCost());
        mantenimiento.setEstado("Pendiente"); // Default status

        return mantenimientoRepository.save(mantenimiento);
    }

    public Mantenimiento updateMantenimiento(UUID id, Mantenimiento mantenimientoDetails) {
        Optional<Mantenimiento> optionalMantenimiento = mantenimientoRepository.findById(id);
        if (optionalMantenimiento.isPresent()) {
            Mantenimiento mantenimiento = optionalMantenimiento.get();
            // Only update fields that can be changed, don't change maquina
            if (mantenimientoDetails.getFecha() != null) {
                mantenimiento.setFecha(mantenimientoDetails.getFecha());
            }
            if (mantenimientoDetails.getTipo() != null) {
                mantenimiento.setTipo(mantenimientoDetails.getTipo());
            }
            if (mantenimientoDetails.getTecnico() != null) {
                mantenimiento.setTecnico(mantenimientoDetails.getTecnico());
            }
            if (mantenimientoDetails.getDescripcion() != null) {
                mantenimiento.setDescripcion(mantenimientoDetails.getDescripcion());
            }
            if (mantenimientoDetails.getCosto() != null) {
                mantenimiento.setCosto(mantenimientoDetails.getCosto());
            }
            if (mantenimientoDetails.getEstado() != null) {
                mantenimiento.setEstado(mantenimientoDetails.getEstado());
            }
            return mantenimientoRepository.save(mantenimiento);
        }
        return null;
    }

    public boolean deleteMantenimiento(UUID id) {
        if (mantenimientoRepository.existsById(id)) {
            mantenimientoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}