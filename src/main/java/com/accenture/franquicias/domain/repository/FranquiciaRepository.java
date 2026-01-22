package com.accenture.franquicias.domain.repository;

import com.accenture.franquicias.domain.model.Franquicia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    
    Mono<Franquicia> guardar(Franquicia franquicia);
    
    Mono<Franquicia> buscarPorId(Long id);
    
    Flux<Franquicia> buscarTodas();
    
    Mono<Boolean> existePorNombre(String nombre);
    
    Mono<Boolean> existePorNombreYIdDiferente(String nombre, Long id);
    
    Mono<Void> eliminar(Long id);
}
