package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.Alquiler;
import com.maquinarias.maquinarias.model.CreateAlquilerRequest;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.model.User;
import com.maquinarias.maquinarias.repository.AlquilerRepository;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import com.maquinarias.maquinarias.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private MaquinaRepository maquinaRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Alquiler> getAllAlquileres() {
        return alquilerRepository.findAll();
    }

    public Optional<Alquiler> getAlquilerById(UUID id) {
        return alquilerRepository.findById(id);
    }

    public Alquiler createAlquiler(CreateAlquilerRequest request) {
        Alquiler alquiler = new Alquiler();
        Maquina maquina = maquinaRepository.findById(request.getMachineryId()).orElseThrow(() -> new RuntimeException("Maquina not found"));
        alquiler.setMaquina(maquina);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        alquiler.setUser(user);

        alquiler.setFechaInicio(request.getStartDate());
        alquiler.setFechaFin(request.getEndDate());
        alquiler.setCostoTotal(request.getCost());
        alquiler.setEstado(request.getStatus());

        return alquilerRepository.save(alquiler);
    }

    public Alquiler updateAlquiler(UUID id, Alquiler alquilerDetails) {
        Optional<Alquiler> optionalAlquiler = alquilerRepository.findById(id);
        if (optionalAlquiler.isPresent()) {
            Alquiler alquiler = optionalAlquiler.get();
            // Only update fields that can be changed, don't change maquina or user
            if (alquilerDetails.getFechaInicio() != null) {
                alquiler.setFechaInicio(alquilerDetails.getFechaInicio());
            }
            if (alquilerDetails.getFechaFin() != null) {
                alquiler.setFechaFin(alquilerDetails.getFechaFin());
            }
            if (alquilerDetails.getCostoTotal() != null) {
                alquiler.setCostoTotal(alquilerDetails.getCostoTotal());
            }
            if (alquilerDetails.getEstado() != null) {
                alquiler.setEstado(alquilerDetails.getEstado());
            }
            return alquilerRepository.save(alquiler);
        }
        return null;
    }

    public boolean deleteAlquiler(UUID id) {
        if (alquilerRepository.existsById(id)) {
            alquilerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}