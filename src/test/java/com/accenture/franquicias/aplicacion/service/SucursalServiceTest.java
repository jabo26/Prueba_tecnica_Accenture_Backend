package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.SucursalRequest;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.SucursalRepositoryR2dbc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepositoryR2dbc repository;

    @Mock
    private FranquiciaRepositoryR2dbc franquiciaRepository;

    @InjectMocks
    private SucursalService service;

    @Test
    void crear_franquiciaNoExiste_lanzaEntidadNoEncontradaException() {
        SucursalRequest request = new SucursalRequest("Centro", 999L);
        when(franquiciaRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(service.crear(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("Franquicia") &&
                        throwable.getMessage().contains("999"))
                .verify();

        verify(repository, never()).existsByNombreAndFranquiciaId(anyString(), anyLong());
    }

    @Test
    void crear_nombreDuplicadoEnMismaFranquicia_lanzaNombreDuplicadoException() {
        SucursalRequest request = new SucursalRequest("Centro", 1L);

        Franquicia franquiciaExistente = Franquicia.builder()
                .id(1L)
                .nombre("McDonald's")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquiciaExistente));
        when(repository.existsByNombreAndFranquiciaId("Centro", 1L)).thenReturn(Mono.just(1));

        StepVerifier.create(service.crear(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof NombreDuplicadoException &&
                        throwable.getMessage().contains("Sucursal") &&
                        throwable.getMessage().contains("Centro"))
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void crear_nombreDisponible_guardaYRetornaRespuesta() {
        SucursalRequest request = new SucursalRequest("Centro", 1L);

        Franquicia franquiciaExistente = Franquicia.builder()
                .id(1L)
                .nombre("McDonald's")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        Sucursal sucursalGuardada = Sucursal.builder()
                .id(1L)
                .nombre("Centro")
                .franquiciaId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(franquiciaRepository.findById(1L)).thenReturn(Mono.just(franquiciaExistente));
        when(repository.existsByNombreAndFranquiciaId("Centro", 1L)).thenReturn(Mono.just(0));
        when(repository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursalGuardada));

        StepVerifier.create(service.crear(request))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert "Centro".equals(response.getNombre());
                    assert response.getFranquiciaId().equals(1L);
                })
                .verifyComplete();
    }

    @Test
    void buscarPorId_existe_retornaRespuesta() {
        Sucursal sucursal = Sucursal.builder()
                .id(2L)
                .nombre("Norte")
                .franquiciaId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(repository.findById(2L)).thenReturn(Mono.just(sucursal));

        StepVerifier.create(service.buscarPorId(2L))
                .assertNext(response -> {
                    assert response.getId().equals(2L);
                    assert "Norte".equals(response.getNombre());
                    assert response.getFranquiciaId().equals(1L);
                })
                .verifyComplete();
    }

    @Test
    void buscarPorId_noExiste_lanzaEntidadNoEncontradaException() {
        when(repository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(service.buscarPorId(999L))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("Sucursal") &&
                        throwable.getMessage().contains("999"))
                .verify();
    }
}