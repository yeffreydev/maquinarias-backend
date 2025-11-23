package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.CreateMantenimientoRequest;
import com.maquinarias.maquinarias.model.Mantenimiento;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.repository.MantenimientoRepository;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @Mock
    private MaquinaRepository maquinaRepository;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    private Mantenimiento mantenimiento;
    private UUID mantenimientoId;
    private Maquina maquina;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mantenimientoId = UUID.randomUUID();

        maquina = new Maquina();
        maquina.setId(UUID.randomUUID());
        maquina.setCodigo("M001");

        mantenimiento = new Mantenimiento();
        mantenimiento.setId(mantenimientoId);
        mantenimiento.setMaquina(maquina);
        mantenimiento.setFecha(LocalDateTime.now());
        mantenimiento.setTipo("Preventivo");
        mantenimiento.setDescripcion("Mantenimiento rutinario");
        mantenimiento.setCosto(200.0);
        mantenimiento.setEstado("Completado");
    }

    @Test
    void testGetAllMantenimientos() {
        List<Mantenimiento> mantenimientos = Arrays.asList(mantenimiento);
        when(mantenimientoRepository.findAll()).thenReturn(mantenimientos);

        List<Mantenimiento> result = mantenimientoService.getAllMantenimientos();

        assertEquals(1, result.size());
        assertEquals(mantenimiento, result.get(0));
        verify(mantenimientoRepository, times(1)).findAll();
    }

    @Test
    void testGetMantenimientoById() {
        when(mantenimientoRepository.findById(mantenimientoId)).thenReturn(Optional.of(mantenimiento));

        Optional<Mantenimiento> result = mantenimientoService.getMantenimientoById(mantenimientoId);

        assertTrue(result.isPresent());
        assertEquals(mantenimiento, result.get());
        verify(mantenimientoRepository, times(1)).findById(mantenimientoId);
    }

    @Test
    void testCreateMantenimiento() {
        CreateMantenimientoRequest request = new CreateMantenimientoRequest();
        request.setMachineryId(maquina.getId());
        request.setMaintenanceType("preventive");
        request.setDate(LocalDateTime.now());
        request.setTechnician("yeffrey");
        request.setDescription("test");

        when(maquinaRepository.findById(maquina.getId())).thenReturn(Optional.of(maquina));
        when(mantenimientoRepository.save(any(Mantenimiento.class))).thenReturn(mantenimiento);

        Mantenimiento result = mantenimientoService.createMantenimiento(request);

        assertEquals(mantenimiento, result);
        verify(maquinaRepository, times(1)).findById(maquina.getId());
        verify(mantenimientoRepository, times(1)).save(any(Mantenimiento.class));
    }

    @Test
    void testUpdateMantenimiento() {
        Mantenimiento updatedMantenimiento = new Mantenimiento();
        updatedMantenimiento.setCosto(250.0);
        updatedMantenimiento.setEstado("En Progreso");

        when(mantenimientoRepository.findById(mantenimientoId)).thenReturn(Optional.of(mantenimiento));
        when(mantenimientoRepository.save(any(Mantenimiento.class))).thenReturn(mantenimiento);

        Mantenimiento result = mantenimientoService.updateMantenimiento(mantenimientoId, updatedMantenimiento);

        assertNotNull(result);
        assertEquals(250.0, result.getCosto());
        assertEquals("En Progreso", result.getEstado());
        verify(mantenimientoRepository, times(1)).findById(mantenimientoId);
        verify(mantenimientoRepository, times(1)).save(mantenimiento);
    }

    @Test
    void testUpdateMantenimientoNotFound() {
        when(mantenimientoRepository.findById(mantenimientoId)).thenReturn(Optional.empty());

        Mantenimiento result = mantenimientoService.updateMantenimiento(mantenimientoId, mantenimiento);

        assertNull(result);
        verify(mantenimientoRepository, times(1)).findById(mantenimientoId);
        verify(mantenimientoRepository, never()).save(any(Mantenimiento.class));
    }

    @Test
    void testDeleteMantenimiento() {
        when(mantenimientoRepository.existsById(mantenimientoId)).thenReturn(true);

        boolean result = mantenimientoService.deleteMantenimiento(mantenimientoId);

        assertTrue(result);
        verify(mantenimientoRepository, times(1)).existsById(mantenimientoId);
        verify(mantenimientoRepository, times(1)).deleteById(mantenimientoId);
    }

    @Test
    void testDeleteMantenimientoNotFound() {
        when(mantenimientoRepository.existsById(mantenimientoId)).thenReturn(false);

        boolean result = mantenimientoService.deleteMantenimiento(mantenimientoId);

        assertFalse(result);
        verify(mantenimientoRepository, times(1)).existsById(mantenimientoId);
        verify(mantenimientoRepository, never()).deleteById(mantenimientoId);
    }
}