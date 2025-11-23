package com.maquinarias.maquinarias.controller;

import com.maquinarias.maquinarias.model.Alquiler;
import com.maquinarias.maquinarias.model.CreateAlquilerRequest;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.model.User;
import com.maquinarias.maquinarias.service.AlquilerService;
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

class AlquilerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AlquilerService alquilerService;

    @InjectMocks
    private AlquilerController alquilerController;

    private Alquiler alquiler;
    private UUID alquilerId;
    private Maquina maquina;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alquilerController).build();

        alquilerId = UUID.randomUUID();

        maquina = new Maquina();
        maquina.setId(UUID.randomUUID());
        maquina.setCodigo("M001");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        alquiler = new Alquiler();
        alquiler.setId(alquilerId);
        alquiler.setMaquina(maquina);
        alquiler.setUser(user);
        alquiler.setFechaInicio(LocalDateTime.now());
        alquiler.setFechaFin(LocalDateTime.now().plusDays(1));
        alquiler.setCostoTotal(100.0);
        alquiler.setEstado("Activo");
    }

    @Test
    void testGetAllAlquileres() throws Exception {
        List<Alquiler> alquileres = Arrays.asList(alquiler);
        when(alquilerService.getAllAlquileres()).thenReturn(alquileres);

        mockMvc.perform(get("/api/alquileres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(alquilerService, times(1)).getAllAlquileres();
    }

    @Test
    void testGetAlquilerById() throws Exception {
        when(alquilerService.getAlquilerById(alquilerId)).thenReturn(Optional.of(alquiler));

        mockMvc.perform(get("/api/alquileres/{id}", alquilerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(alquilerService, times(1)).getAlquilerById(alquilerId);
    }

    @Test
    void testGetAlquilerByIdNotFound() throws Exception {
        when(alquilerService.getAlquilerById(alquilerId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/alquileres/{id}", alquilerId))
                .andExpect(status().isNotFound());

        verify(alquilerService, times(1)).getAlquilerById(alquilerId);
    }

    @Test
    void testCreateAlquiler() throws Exception {
        when(alquilerService.createAlquiler(any(CreateAlquilerRequest.class))).thenReturn(alquiler);

        mockMvc.perform(post("/api/alquileres")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineryId\":\"" + maquina.getId() + "\",\"startDate\":\"2023-01-01T10:00:00\",\"endDate\":\"2023-01-02T10:00:00\",\"cost\":100.0,\"status\":\"Activo\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(alquilerService, times(1)).createAlquiler(any(CreateAlquilerRequest.class));
    }

    @Test
    void testUpdateAlquiler() throws Exception {
        when(alquilerService.updateAlquiler(eq(alquilerId), any(Alquiler.class))).thenReturn(alquiler);

        mockMvc.perform(put("/api/alquileres/{id}", alquilerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"costoTotal\":150.0,\"estado\":\"Completado\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(alquilerService, times(1)).updateAlquiler(eq(alquilerId), any(Alquiler.class));
    }

    @Test
    void testUpdateAlquilerNotFound() throws Exception {
        when(alquilerService.updateAlquiler(eq(alquilerId), any(Alquiler.class))).thenReturn(null);

        mockMvc.perform(put("/api/alquileres/{id}", alquilerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"costoTotal\":150.0}"))
                .andExpect(status().isNotFound());

        verify(alquilerService, times(1)).updateAlquiler(eq(alquilerId), any(Alquiler.class));
    }

    @Test
    void testDeleteAlquiler() throws Exception {
        when(alquilerService.deleteAlquiler(alquilerId)).thenReturn(true);

        mockMvc.perform(delete("/api/alquileres/{id}", alquilerId))
                .andExpect(status().isNoContent());

        verify(alquilerService, times(1)).deleteAlquiler(alquilerId);
    }

    @Test
    void testDeleteAlquilerNotFound() throws Exception {
        when(alquilerService.deleteAlquiler(alquilerId)).thenReturn(false);

        mockMvc.perform(delete("/api/alquileres/{id}", alquilerId))
                .andExpect(status().isNotFound());

        verify(alquilerService, times(1)).deleteAlquiler(alquilerId);
    }
}