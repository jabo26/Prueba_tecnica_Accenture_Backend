package com.accenture.franquicias.infraestructura.repository;

import com.accenture.franquicias.domain.model.Franquicia;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranquiciaRepositoryR2dbc extends R2dbcRepository<Franquicia, Long> {
    
    @Query("SELECT COUNT(*) > 0 FROM franquicias WHERE nombre = :nombre")
    Mono<Boolean> existsByNombre(String nombre);
    
    @Query("SELECT COUNT(*) > 0 FROM franquicias WHERE nombre = :nombre AND id != :id")
    Mono<Boolean> existsByNombreAndIdNot(String nombre, Long id);
}
