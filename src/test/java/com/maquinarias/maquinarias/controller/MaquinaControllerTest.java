package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.service.MaquinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MaquinaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaquinaService maquinaService;

    @InjectMocks
    private MaquinaController maquinaController;

    private Maquina maquina;
    private UUID maquinaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(maquinaController).build();

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
    void testGetAllMaquinas() throws Exception {
        List<Maquina> maquinas = Arrays.asList(maquina);
        when(maquinaService.getAllMaquinas()).thenReturn(maquinas);

        mockMvc.perform(get("/api/maquinas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(maquinaService, times(1)).getAllMaquinas();
    }

    @Test
    void testGetMaquinaById() throws Exception {
        when(maquinaService.getMaquinaById(maquinaId)).thenReturn(Optional.of(maquina));

        mockMvc.perform(get("/api/maquinas/{id}", maquinaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(maquinaService, times(1)).getMaquinaById(maquinaId);
    }

    @Test
    void testGetMaquinaByIdNotFound() throws Exception {
        when(maquinaService.getMaquinaById(maquinaId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/maquinas/{id}", maquinaId))
                .andExpect(status().isNotFound());

        verify(maquinaService, times(1)).getMaquinaById(maquinaId);
    }

    @Test
    void testCreateMaquina() throws Exception {
        when(maquinaService.createMaquina(any(Maquina.class))).thenReturn(maquina);

        mockMvc.perform(post("/api/maquinas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"M001\",\"modelo\":\"Modelo A\",\"tipo\":\"Excavadora\",\"estado\":\"Disponible\",\"ubicacion\":\"Sitio A\",\"tarifaHora\":50.0,\"imagen\":\"imagen.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(maquinaService, times(1)).createMaquina(any(Maquina.class));
    }

    @Test
    void testUpdateMaquina() throws Exception {
        when(maquinaService.updateMaquina(eq(maquinaId), any(Maquina.class))).thenReturn(maquina);

        mockMvc.perform(put("/api/maquinas/{id}", maquinaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"M002\",\"modelo\":\"Modelo B\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(maquinaService, times(1)).updateMaquina(eq(maquinaId), any(Maquina.class));
    }

    @Test
    void testUpdateMaquinaNotFound() throws Exception {
        when(maquinaService.updateMaquina(eq(maquinaId), any(Maquina.class))).thenReturn(null);

        mockMvc.perform(put("/api/maquinas/{id}", maquinaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"M002\"}"))
                .andExpect(status().isNotFound());

        verify(maquinaService, times(1)).updateMaquina(eq(maquinaId), any(Maquina.class));
    }

    @Test
    void testDeleteMaquina() throws Exception {
        when(maquinaService.deleteMaquina(maquinaId)).thenReturn(true);

        mockMvc.perform(delete("/api/maquinas/{id}", maquinaId))
                .andExpect(status().isNoContent());

        verify(maquinaService, times(1)).deleteMaquina(maquinaId);
    }

    @Test
    void testDeleteMaquinaNotFound() throws Exception {
        when(maquinaService.deleteMaquina(maquinaId)).thenReturn(false);

        mockMvc.perform(delete("/api/maquinas/{id}", maquinaId))
                .andExpect(status().isNotFound());

        verify(maquinaService, times(1)).deleteMaquina(maquinaId);
    }
}