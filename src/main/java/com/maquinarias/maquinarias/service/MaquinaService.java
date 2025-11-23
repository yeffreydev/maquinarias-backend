package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaquinaService {

    @Autowired
    private MaquinaRepository maquinaRepository;

    public List<Maquina> getAllMaquinas() {
        return maquinaRepository.findAll();
    }

    public Optional<Maquina> getMaquinaById(UUID id) {
        return maquinaRepository.findById(id);
    }

    public Maquina createMaquina(Maquina maquina) {
        return maquinaRepository.save(maquina);
    }

    public Maquina updateMaquina(UUID id, Maquina maquinaDetails) {
        Optional<Maquina> optionalMaquina = maquinaRepository.findById(id);
        if (optionalMaquina.isPresent()) {
            Maquina maquina = optionalMaquina.get();
            maquina.setCodigo(maquinaDetails.getCodigo());
            maquina.setModelo(maquinaDetails.getModelo());
            maquina.setTipo(maquinaDetails.getTipo());
            maquina.setEstado(maquinaDetails.getEstado());
            maquina.setUbicacion(maquinaDetails.getUbicacion());
            maquina.setTarifaHora(maquinaDetails.getTarifaHora());
            maquina.setImagen(maquinaDetails.getImagen());
            maquina.setPotencia(maquinaDetails.getPotencia());
            maquina.setCapacidad(maquinaDetails.getCapacidad());
            maquina.setPeso(maquinaDetails.getPeso());
            maquina.setDimensiones(maquinaDetails.getDimensiones());
            return maquinaRepository.save(maquina);
        }
        return null;
    }

    public boolean deleteMaquina(UUID id) {
        if (maquinaRepository.existsById(id)) {
            maquinaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}