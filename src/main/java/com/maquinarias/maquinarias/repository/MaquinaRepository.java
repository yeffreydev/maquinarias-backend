package com.maquinarias.maquinarias.repository;

import com.maquinarias.maquinarias.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, UUID> {
}