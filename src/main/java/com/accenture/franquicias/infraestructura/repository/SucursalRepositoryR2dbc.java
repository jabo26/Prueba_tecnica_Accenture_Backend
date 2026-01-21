package com.accenture.franquicias.infraestructura.repository;

import com.accenture.franquicias.domain.model.Sucursal;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SucursalRepositoryR2dbc extends R2dbcRepository<Sucursal, Long> {
    
    Flux<Sucursal> findByFranquiciaId(Long franquiciaId);
    
    @Query("SELECT COUNT(*) > 0 FROM sucursales WHERE nombre = :nombre AND franquicia_id = :franquiciaId")
    Mono<Boolean> existsByNombreAndFranquiciaId(String nombre, Long franquiciaId);
    
    @Query("SELECT COUNT(*) > 0 FROM sucursales WHERE nombre = :nombre AND franquicia_id = :franquiciaId AND id != :id")
    Mono<Boolean> existsByNombreAndFranquiciaIdAndIdNot(String nombre, Long franquiciaId, Long id);
}
