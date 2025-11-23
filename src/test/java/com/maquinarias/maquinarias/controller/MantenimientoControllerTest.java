package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.CreateMantenimientoRequest;
import com.maquinarias.maquinarias.model.Mantenimiento;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.service.MantenimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MantenimientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MantenimientoService mantenimientoService;

    @InjectMocks
    private MantenimientoController mantenimientoController;

    private Mantenimiento mantenimiento;
    private UUID mantenimientoId;
    private Maquina maquina;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mantenimientoController).build();

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
    void testGetAllMantenimientos() throws Exception {
        List<Mantenimiento> mantenimientos = Arrays.asList(mantenimiento);
        when(mantenimientoService.getAllMantenimientos()).thenReturn(mantenimientos);

        mockMvc.perform(get("/api/mantenimientos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mantenimientoService, times(1)).getAllMantenimientos();
    }

    @Test
    void testGetMantenimientoById() throws Exception {
        when(mantenimientoService.getMantenimientoById(mantenimientoId)).thenReturn(Optional.of(mantenimiento));

        mockMvc.perform(get("/api/mantenimientos/{id}", mantenimientoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mantenimientoService, times(1)).getMantenimientoById(mantenimientoId);
    }

    @Test
    void testGetMantenimientoByIdNotFound() throws Exception {
        when(mantenimientoService.getMantenimientoById(mantenimientoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/mantenimientos/{id}", mantenimientoId))
                .andExpect(status().isNotFound());

        verify(mantenimientoService, times(1)).getMantenimientoById(mantenimientoId);
    }

    @Test
    void testCreateMantenimiento() throws Exception {
        when(mantenimientoService.createMantenimiento(any(CreateMantenimientoRequest.class))).thenReturn(mantenimiento);

        mockMvc.perform(post("/api/mantenimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineryId\":\"" + maquina.getId() + "\",\"maintenanceType\":\"preventive\",\"date\":\"2025-11-04T00:00:00.000Z\",\"technician\":\"yeffrey\",\"description\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mantenimientoService, times(1)).createMantenimiento(any(CreateMantenimientoRequest.class));
    }

    @Test
    void testUpdateMantenimiento() throws Exception {
        when(mantenimientoService.updateMantenimiento(eq(mantenimientoId), any(Mantenimiento.class))).thenReturn(mantenimiento);

        mockMvc.perform(put("/api/mantenimientos/{id}", mantenimientoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"costo\":250.0,\"estado\":\"En Progreso\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mantenimientoService, times(1)).updateMantenimiento(eq(mantenimientoId), any(Mantenimiento.class));
    }

    @Test
    void testUpdateMantenimientoNotFound() throws Exception {
        when(mantenimientoService.updateMantenimiento(eq(mantenimientoId), any(Mantenimiento.class))).thenReturn(null);

        mockMvc.perform(put("/api/mantenimientos/{id}", mantenimientoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"costo\":250.0}"))
                .andExpect(status().isNotFound());

        verify(mantenimientoService, times(1)).updateMantenimiento(eq(mantenimientoId), any(Mantenimiento.class));
    }

    @Test
    void testDeleteMantenimiento() throws Exception {
        when(mantenimientoService.deleteMantenimiento(mantenimientoId)).thenReturn(true);

        mockMvc.perform(delete("/api/mantenimientos/{id}", mantenimientoId))
                .andExpect(status().isNoContent());

        verify(mantenimientoService, times(1)).deleteMantenimiento(mantenimientoId);
    }

    @Test
    void testDeleteMantenimientoNotFound() throws Exception {
        when(mantenimientoService.deleteMantenimiento(mantenimientoId)).thenReturn(false);

        mockMvc.perform(delete("/api/mantenimientos/{id}", mantenimientoId))
                .andExpect(status().isNotFound());

        verify(mantenimientoService, times(1)).deleteMantenimiento(mantenimientoId);
    }
}