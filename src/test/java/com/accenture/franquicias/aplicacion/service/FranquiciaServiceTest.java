package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.FranquiciaRequest;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepositoryR2dbc repository;

    @InjectMocks
    private FranquiciaService service;

    @Test
    void crear_nombreDisponible_guardaYRetornaRespuesta() {
        FranquiciaRequest request = new FranquiciaRequest("Burger King");
        Franquicia franquiciaGuardada = Franquicia.builder()
                .id(1L)
                .nombre("Burger King")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(repository.existsByNombre("Burger King")).thenReturn(Mono.just(0));
        when(repository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaGuardada));

        StepVerifier.create(service.crear(request))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert "Burger King".equals(response.getNombre());
                })
                .verifyComplete();
    }

    @Test
    void crear_nombreDuplicado_lanzaNombreDuplicadoException() {
        FranquiciaRequest request = new FranquiciaRequest("McDonald's");
        when(repository.existsByNombre("McDonald's")).thenReturn(Mono.just(1));

        StepVerifier.create(service.crear(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof NombreDuplicadoException &&
                        throwable.getMessage().contains("McDonald's"))
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void buscarPorId_existe_retornaRespuesta() {
        Franquicia franquicia = Franquicia.builder()
                .id(2L)
                .nombre("KFC")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(repository.findById(2L)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.buscarPorId(2L))
                .assertNext(response -> {
                    assert response.getId().equals(2L);
                    assert "KFC".equals(response.getNombre());
                })
                .verifyComplete();
    }

    @Test
    void buscarPorId_noExiste_lanzaEntidadNoEncontradaException() {
        when(repository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(service.buscarPorId(999L))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("999"))
                .verify();
    }
}