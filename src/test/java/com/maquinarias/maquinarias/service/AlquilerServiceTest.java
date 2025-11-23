package com.maquinarias.maquinarias.service;

import com.maquinarias.maquinarias.model.Alquiler;
import com.maquinarias.maquinarias.model.CreateAlquilerRequest;
import com.maquinarias.maquinarias.model.Maquina;
import com.maquinarias.maquinarias.model.User;
import com.maquinarias.maquinarias.repository.AlquilerRepository;
import com.maquinarias.maquinarias.repository.MaquinaRepository;
import com.maquinarias.maquinarias.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlquilerServiceTest {

    @Mock
    private AlquilerRepository alquilerRepository;

    @Mock
    private MaquinaRepository maquinaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AlquilerService alquilerService;

    private Alquiler alquiler;
    private UUID alquilerId;
    private Maquina maquina;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        // Setup authentication context
        when(authentication.getName()).thenReturn("test@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllAlquileres() {
        List<Alquiler> alquileres = Arrays.asList(alquiler);
        when(alquilerRepository.findAll()).thenReturn(alquileres);

        List<Alquiler> result = alquilerService.getAllAlquileres();

        assertEquals(1, result.size());
        assertEquals(alquiler, result.get(0));
        verify(alquilerRepository, times(1)).findAll();
    }

    @Test
    void testGetAlquilerById() {
        when(alquilerRepository.findById(alquilerId)).thenReturn(Optional.of(alquiler));

        Optional<Alquiler> result = alquilerService.getAlquilerById(alquilerId);

        assertTrue(result.isPresent());
        assertEquals(alquiler, result.get());
        verify(alquilerRepository, times(1)).findById(alquilerId);
    }

    @Test
    void testCreateAlquiler() {
        CreateAlquilerRequest request = new CreateAlquilerRequest();
        request.setMachineryId(maquina.getId());
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(1));
        request.setCost(100.0);
        request.setStatus("Activo");

        when(maquinaRepository.findById(maquina.getId())).thenReturn(Optional.of(maquina));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(alquilerRepository.save(any(Alquiler.class))).thenReturn(alquiler);

        Alquiler result = alquilerService.createAlquiler(request);

        assertEquals(alquiler, result);
        verify(maquinaRepository, times(1)).findById(maquina.getId());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(alquilerRepository, times(1)).save(any(Alquiler.class));
    }

    @Test
    void testUpdateAlquiler() {
        Alquiler updatedAlquiler = new Alquiler();
        updatedAlquiler.setCostoTotal(150.0);
        updatedAlquiler.setEstado("Completado");

        when(alquilerRepository.findById(alquilerId)).thenReturn(Optional.of(alquiler));
        when(alquilerRepository.save(any(Alquiler.class))).thenReturn(alquiler);

        Alquiler result = alquilerService.updateAlquiler(alquilerId, updatedAlquiler);

        assertNotNull(result);
        assertEquals(150.0, result.getCostoTotal());
        assertEquals("Completado", result.getEstado());
        verify(alquilerRepository, times(1)).findById(alquilerId);
        verify(alquilerRepository, times(1)).save(alquiler);
    }

    @Test
    void testUpdateAlquilerNotFound() {
        when(alquilerRepository.findById(alquilerId)).thenReturn(Optional.empty());

        Alquiler result = alquilerService.updateAlquiler(alquilerId, alquiler);

        assertNull(result);
        verify(alquilerRepository, times(1)).findById(alquilerId);
        verify(alquilerRepository, never()).save(any(Alquiler.class));
    }

    @Test
    void testDeleteAlquiler() {
        when(alquilerRepository.existsById(alquilerId)).thenReturn(true);

        boolean result = alquilerService.deleteAlquiler(alquilerId);

        assertTrue(result);
        verify(alquilerRepository, times(1)).existsById(alquilerId);
        verify(alquilerRepository, times(1)).deleteById(alquilerId);
    }

    @Test
    void testDeleteAlquilerNotFound() {
        when(alquilerRepository.existsById(alquilerId)).thenReturn(false);

        boolean result = alquilerService.deleteAlquiler(alquilerId);

        assertFalse(result);
        verify(alquilerRepository, times(1)).existsById(alquilerId);
        verify(alquilerRepository, never()).deleteById(alquilerId);
    }
}