package com.accenture.franquicias.domain.repository;

import com.accenture.franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository {
    
    Mono<Producto> guardar(Producto producto);
    
    Mono<Producto> buscarPorId(Long id);
    
    Flux<Producto> buscarPorSucursalId(Long sucursalId);
    
    Flux<Producto> buscarTodos();
    
    Mono<Boolean> existePorNombreYSucursalId(String nombre, Long sucursalId);
    
    Mono<Boolean> existePorNombreYSucursalIdYIdDiferente(String nombre, Long sucursalId, Long id);
    
    Mono<Void> eliminar(Long id);    
    
    Flux<Producto> buscarProductoConMasStockPorSucursalEnFranquicia(Long franquiciaId);
}
