package com.accenture.franquicias.infraestructura.web;

import com.accenture.franquicias.aplicacion.dto.*;
import com.accenture.franquicias.aplicacion.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService service;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return service.crear(request);
    }
    
    @PutMapping("/{id}/nombre")
    public Mono<ProductoResponse> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return service.actualizarNombre(id, request);
    }
    
    @PutMapping("/{id}/stock")
    public Mono<ProductoResponse> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarStockRequest request) {
        return service.actualizarStock(id, request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }
    
    @GetMapping("/sucursal/{sucursalId}")
    public Flux<ProductoResponse> listarPorSucursal(@PathVariable Long sucursalId) {
        return service.listarPorSucursal(sucursalId);
    }
    
    @GetMapping("/mas-stock/franquicia/{franquiciaId}")
    public Flux<ProductoStockResponse> obtenerProductosConMasStock(@PathVariable Long franquiciaId) {
        return service.obtenerProductosConMasStockPorFranquicia(franquiciaId);
    }
}
