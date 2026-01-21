package com.accenture.franquicias.domain.repository;

import com.accenture.franquicias.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepository {
    
    Mono<Sucursal> guardar(Sucursal sucursal);
    
    Mono<Sucursal> buscarPorId(Long id);
    
    Flux<Sucursal> buscarPorFranquiciaId(Long franquiciaId);
    
    Flux<Sucursal> buscarTodas();
    
    Mono<Boolean> existePorNombreYFranquiciaId(String nombre, Long franquiciaId);
    
    Mono<Boolean> existePorNombreYFranquiciaIdYIdDiferente(String nombre, Long franquiciaId, Long id);
    
    Mono<Void> eliminar(Long id);
}
