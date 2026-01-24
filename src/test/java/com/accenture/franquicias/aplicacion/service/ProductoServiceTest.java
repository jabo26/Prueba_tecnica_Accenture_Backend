package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.ActualizarStockRequest;
import com.accenture.franquicias.aplicacion.dto.ProductoRequest;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Producto;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.infraestructura.repository.ProductoRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.SucursalRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepositoryR2dbc repository;

    @Mock
    private SucursalRepositoryR2dbc sucursalRepository;

    @Mock
    private FranquiciaRepositoryR2dbc franquiciaRepository;

    @InjectMocks
    private ProductoService service;

    @Test
    void crear_sucursalNoExiste_lanzaEntidadNoEncontradaException() {
        ProductoRequest request = new ProductoRequest("Big Mac", 50, 999L);
        when(sucursalRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(service.crear(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("Sucursal") &&
                        throwable.getMessage().contains("999"))
                .verify();

        verify(repository, never()).existsByNombreAndSucursalId(anyString(), anyLong());
    }

    @Test
    void crear_nombreDuplicadoEnMismaSucursal_lanzaNombreDuplicadoException() {
        ProductoRequest request = new ProductoRequest("Big Mac", 50, 1L);

        Sucursal sucursalExistente = Sucursal.builder()
                .id(1L)
                .nombre("Centro")
                .franquiciaId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursalExistente));
        when(repository.existsByNombreAndSucursalId("Big Mac", 1L)).thenReturn(Mono.just(1));

        StepVerifier.create(service.crear(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof NombreDuplicadoException &&
                        throwable.getMessage().contains("Producto") &&
                        throwable.getMessage().contains("Big Mac"))
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void crear_nombreDisponible_guardaYRetornaRespuesta() {
        ProductoRequest request = new ProductoRequest("Big Mac", 50, 1L);

        Sucursal sucursalExistente = Sucursal.builder()
                .id(1L)
                .nombre("Centro")
                .franquiciaId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        Producto productoGuardado = Producto.builder()
                .id(1L)
                .nombre("Big Mac")
                .stock(50)
                .sucursalId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(sucursalRepository.findById(1L)).thenReturn(Mono.just(sucursalExistente));
        when(repository.existsByNombreAndSucursalId("Big Mac", 1L)).thenReturn(Mono.just(0));
        when(repository.save(any(Producto.class))).thenReturn(Mono.just(productoGuardado));

        StepVerifier.create(service.crear(request))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert "Big Mac".equals(response.getNombre());
                    assert response.getStock() == 50;
                    assert response.getSucursalId().equals(1L);
                })
                .verifyComplete();
    }

    @Test
    void actualizarStock_productoNoExiste_lanzaExcepcion() {
        when(repository.findById(888L)).thenReturn(Mono.empty());

        StepVerifier.create(service.actualizarStock(888L, new ActualizarStockRequest(100)))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("Producto") &&
                        throwable.getMessage().contains("888"))
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void actualizarStock_exitoso() {
        Producto productoExistente = Producto.builder()
                .id(2L)
                .nombre("Whopper")
                .stock(30)
                .sucursalId(1L)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        Producto productoActualizado = Producto.builder()
                .id(2L)
                .nombre("Whopper")
                .stock(75)
                .sucursalId(1L)
                .fechaCreacion(productoExistente.getFechaCreacion())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(repository.findById(2L)).thenReturn(Mono.just(productoExistente));
        when(repository.save(any(Producto.class))).thenReturn(Mono.just(productoActualizado));

        StepVerifier.create(service.actualizarStock(2L, new ActualizarStockRequest(75)))
                .assertNext(response -> {
                    assert response.getStock() == 75;
                    assert response.getId().equals(2L);
                })
                .verifyComplete();
    }

    @Test
    void eliminar_productoNoExiste_lanzaExcepcion() {
        when(repository.findById(777L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminar(777L))
                .expectErrorMatches(throwable ->
                        throwable instanceof EntidadNoEncontradaException &&
                        throwable.getMessage().contains("Producto") &&
                        throwable.getMessage().contains("777"))
                .verify();

        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void eliminar_exitoso() {
        Producto producto = Producto.builder()
                .id(3L)
                .nombre("Fries")
                .stock(20)
                .sucursalId(1L)
                .build();

        when(repository.findById(3L)).thenReturn(Mono.just(producto));
        when(repository.deleteById(3L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminar(3L))
                .verifyComplete();

        verify(repository).deleteById(3L);
    }
}