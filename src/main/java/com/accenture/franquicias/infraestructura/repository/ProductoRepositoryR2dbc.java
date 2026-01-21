package com.accenture.franquicias.infraestructura.repository;

import com.accenture.franquicias.domain.model.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductoRepositoryR2dbc extends R2dbcRepository<Producto, Long> {
    
    Flux<Producto> findBySucursalId(Long sucursalId);
    
    @Query("SELECT COUNT(*) > 0 FROM productos WHERE nombre = :nombre AND sucursal_id = :sucursalId")
    Mono<Boolean> existsByNombreAndSucursalId(String nombre, Long sucursalId);
    
    @Query("SELECT COUNT(*) > 0 FROM productos WHERE nombre = :nombre AND sucursal_id = :sucursalId AND id != :id")
    Mono<Boolean> existsByNombreAndSucursalIdAndIdNot(String nombre, Long sucursalId, Long id);
    
    @Query("""
       pendiente
        """)
    Flux<Producto> findTopStockBySucursalInFranquicia(Long franquiciaId);
}
