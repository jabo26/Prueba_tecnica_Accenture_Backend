package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.*;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Producto;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.ProductoRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.SucursalRepositoryR2dbc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepositoryR2dbc repository;
    private final SucursalRepositoryR2dbc sucursalRepository;
    private final FranquiciaRepositoryR2dbc franquiciaRepository;
    
    public Mono<ProductoResponse> crear(ProductoRequest request) {
        return sucursalRepository.findById(request.getSucursalId())
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Sucursal", request.getSucursalId())))
            .flatMap(sucursal -> 
                repository.existsByNombreAndSucursalId(request.getNombre(), request.getSucursalId())
                    .flatMap(existe -> {
                        if (Boolean.TRUE.equals(existe)) {
                            return Mono.<Producto>error(new NombreDuplicadoException("Producto", request.getNombre()));
                        }
                        
                        Producto producto = Producto.builder()
                            .nombre(request.getNombre())
                            .stock(request.getStock())
                            .sucursalId(request.getSucursalId())
                            .fechaCreacion(LocalDateTime.now())
                            .fechaActualizacion(LocalDateTime.now())
                            .build();
                        
                        return repository.save(producto);
                    })
            )
            .map(this::toResponse);
    }
    
    public Mono<ProductoResponse> actualizarNombre(Long id, ActualizarNombreRequest request) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Producto", id)))
            .flatMap(producto -> 
                repository.existsByNombreAndSucursalIdAndIdNot(request.getNombre(), producto.getSucursalId(), id)
                    .flatMap(existe -> {
                        if (Boolean.TRUE.equals(existe)) {
                            return Mono.<Producto>error(new NombreDuplicadoException("Producto", request.getNombre()));
                        }
                        
                        producto.setNombre(request.getNombre());
                        producto.setFechaActualizacion(LocalDateTime.now());
                        return repository.save(producto);
                    })
            )
            .map(this::toResponse);
    }
    
    public Mono<ProductoResponse> actualizarStock(Long id, ActualizarStockRequest request) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Producto", id)))
            .flatMap(producto -> {
                producto.setStock(request.getStock());
                producto.setFechaActualizacion(LocalDateTime.now());
                return repository.save(producto);
            })
            .map(this::toResponse);
    }
    
    public Mono<Void> eliminar(Long id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Producto", id)))
            .flatMap(producto -> repository.deleteById(id));
    }
    
    public Flux<ProductoResponse> listarPorSucursal(Long sucursalId) {
        return sucursalRepository.findById(sucursalId)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Sucursal", sucursalId)))
            .flatMapMany(s -> repository.findBySucursalId(sucursalId))
            .map(this::toResponse);
    }
    
    public Flux<ProductoStockResponse> obtenerProductosConMasStockPorFranquicia(Long franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Franquicia", franquiciaId)))
            .flatMapMany(f -> repository.findTopStockBySucursalInFranquicia(franquiciaId))
            .flatMap(producto -> 
                sucursalRepository.findById(producto.getSucursalId())
                    .map(sucursal -> ProductoStockResponse.builder()
                        .productoId(producto.getId())
                        .nombreProducto(producto.getNombre())
                        .stock(producto.getStock())
                        .sucursalId(sucursal.getId())
                        .nombreSucursal(sucursal.getNombre())
                        .build()
                    )
            );
    }
    
    private ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
            .id(producto.getId())
            .nombre(producto.getNombre())
            .stock(producto.getStock())
            .sucursalId(producto.getSucursalId())
            .fechaCreacion(producto.getFechaCreacion())
            .fechaActualizacion(producto.getFechaActualizacion())
            .build();
    }
}
