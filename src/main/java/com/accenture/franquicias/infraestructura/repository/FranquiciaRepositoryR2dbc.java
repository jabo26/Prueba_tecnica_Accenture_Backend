package com.accenture.franquicias.infraestructura.repository;

import com.accenture.franquicias.domain.model.Franquicia;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranquiciaRepositoryR2dbc extends R2dbcRepository<Franquicia, Long> {
    
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM franquicias WHERE nombre = :nombre")
    Mono<Integer> existsByNombre(String nombre);
    
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM franquicias WHERE nombre = :nombre AND id != :id")
    Mono<Integer> existsByNombreAndIdNot(String nombre, Long id);
}
