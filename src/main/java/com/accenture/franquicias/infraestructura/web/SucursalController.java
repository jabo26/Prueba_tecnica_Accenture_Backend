package com.accenture.franquicias.infraestructura.web;

import com.accenture.franquicias.aplicacion.dto.ActualizarNombreRequest;
import com.accenture.franquicias.aplicacion.dto.SucursalRequest;
import com.accenture.franquicias.aplicacion.dto.SucursalResponse;
import com.accenture.franquicias.aplicacion.service.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {
    
    private final SucursalService service;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SucursalResponse> crear(@Valid @RequestBody SucursalRequest request) {
        return service.crear(request);
    }
    
    @PutMapping("/{id}/nombre")
    public Mono<SucursalResponse> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return service.actualizarNombre(id, request);
    }
    
    @GetMapping("/franquicia/{franquiciaId}")
    public Flux<SucursalResponse> listarPorFranquicia(@PathVariable Long franquiciaId) {
        return service.listarPorFranquicia(franquiciaId);
    }
    
    @GetMapping
    public Flux<SucursalResponse> listarTodas() {
        return service.listarTodas();
    }
    
    @GetMapping("/{id}")
    public Mono<SucursalResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
