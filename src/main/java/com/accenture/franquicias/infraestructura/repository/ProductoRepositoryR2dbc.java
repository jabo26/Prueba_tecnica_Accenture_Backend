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
    
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM productos WHERE nombre = :nombre AND sucursal_id = :sucursalId")
    Mono<Integer> existsByNombreAndSucursalId(String nombre, Long sucursalId);
    
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM productos WHERE nombre = :nombre AND sucursal_id = :sucursalId AND id != :id")
    Mono<Integer> existsByNombreAndSucursalIdAndIdNot(String nombre, Long sucursalId, Long id);
    
    @Query("""
        SELECT p.* FROM productos p
        INNER JOIN (
            SELECT p2.sucursal_id, MAX(p2.stock) as max_stock
            FROM productos p2
            INNER JOIN sucursales s ON p2.sucursal_id = s.id
            WHERE s.franquicia_id = :franquiciaId
            GROUP BY p2.sucursal_id
        ) max_p ON p.sucursal_id = max_p.sucursal_id AND p.stock = max_p.max_stock
        """)
    Flux<Producto> findTopStockBySucursalInFranquicia(Long franquiciaId);
}
