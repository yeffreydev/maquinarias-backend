package com.maquinarias.maquinarias.repository;

import com.maquinarias.maquinarias.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, UUID> {
}