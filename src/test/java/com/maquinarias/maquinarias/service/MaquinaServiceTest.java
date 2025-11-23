package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaquinaServiceTest {

    @Mock
    private MaquinaRepository maquinaRepository;

    @InjectMocks
    private MaquinaService maquinaService;

    private Maquina maquina;
    private UUID maquinaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        maquinaId = UUID.randomUUID();
        maquina = new Maquina();
        maquina.setId(maquinaId);
        maquina.setCodigo("M001");
        maquina.setModelo("Modelo A");
        maquina.setTipo("Excavadora");
        maquina.setEstado("Disponible");
        maquina.setUbicacion("Sitio A");
        maquina.setTarifaHora(50.0);
        maquina.setImagen("imagen.jpg");
    }

    @Test
    void testGetAllMaquinas() {
        List<Maquina> maquinas = Arrays.asList(maquina);
        when(maquinaRepository.findAll()).thenReturn(maquinas);

        List<Maquina> result = maquinaService.getAllMaquinas();

        assertEquals(1, result.size());
        assertEquals(maquina, result.get(0));
        verify(maquinaRepository, times(1)).findAll();
    }

    @Test
    void testGetMaquinaById() {
        when(maquinaRepository.findById(maquinaId)).thenReturn(Optional.of(maquina));

        Optional<Maquina> result = maquinaService.getMaquinaById(maquinaId);

        assertTrue(result.isPresent());
        assertEquals(maquina, result.get());
        verify(maquinaRepository, times(1)).findById(maquinaId);
    }

    @Test
    void testCreateMaquina() {
        when(maquinaRepository.save(any(Maquina.class))).thenReturn(maquina);

        Maquina result = maquinaService.createMaquina(maquina);

        assertEquals(maquina, result);
        verify(maquinaRepository, times(1)).save(maquina);
    }

    @Test
    void testUpdateMaquina() {
        Maquina updatedMaquina = new Maquina();
        updatedMaquina.setCodigo("M002");
        updatedMaquina.setModelo("Modelo B");

        when(maquinaRepository.findById(maquinaId)).thenReturn(Optional.of(maquina));
        when(maquinaRepository.save(any(Maquina.class))).thenReturn(maquina);

        Maquina result = maquinaService.updateMaquina(maquinaId, updatedMaquina);

        assertNotNull(result);
        assertEquals("M002", result.getCodigo());
        assertEquals("Modelo B", result.getModelo());
        verify(maquinaRepository, times(1)).findById(maquinaId);
        verify(maquinaRepository, times(1)).save(maquina);
    }

    @Test
    void testUpdateMaquinaNotFound() {
        when(maquinaRepository.findById(maquinaId)).thenReturn(Optional.empty());

        Maquina result = maquinaService.updateMaquina(maquinaId, maquina);

        assertNull(result);
        verify(maquinaRepository, times(1)).findById(maquinaId);
        verify(maquinaRepository, never()).save(any(Maquina.class));
    }

    @Test
    void testDeleteMaquina() {
        when(maquinaRepository.existsById(maquinaId)).thenReturn(true);

        boolean result = maquinaService.deleteMaquina(maquinaId);

        assertTrue(result);
        verify(maquinaRepository, times(1)).existsById(maquinaId);
        verify(maquinaRepository, times(1)).deleteById(maquinaId);
    }

    @Test
    void testDeleteMaquinaNotFound() {
        when(maquinaRepository.existsById(maquinaId)).thenReturn(false);

        boolean result = maquinaService.deleteMaquina(maquinaId);

        assertFalse(result);
        verify(maquinaRepository, times(1)).existsById(maquinaId);
        verify(maquinaRepository, never()).deleteById(maquinaId);
    }
}