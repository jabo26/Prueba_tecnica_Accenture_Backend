package com.accenture.franquicias.infraestructura.web;

import com.accenture.franquicias.aplicacion.dto.ActualizarNombreRequest;
import com.accenture.franquicias.aplicacion.dto.FranquiciaRequest;
import com.accenture.franquicias.aplicacion.dto.FranquiciaResponse;
import com.accenture.franquicias.aplicacion.service.FranquiciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {
    
    private final FranquiciaService service;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranquiciaResponse> crear(@Valid @RequestBody FranquiciaRequest request) {
        return service.crear(request);
    }
    
    @PutMapping("/{id}/nombre")
    public Mono<FranquiciaResponse> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return service.actualizarNombre(id, request);
    }
    
    @GetMapping
    public Flux<FranquiciaResponse> listarTodas() {
        return service.listarTodas();
    }
    
    @GetMapping("/{id}")
    public Mono<FranquiciaResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
