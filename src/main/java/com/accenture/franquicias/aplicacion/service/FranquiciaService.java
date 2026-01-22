package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.ActualizarNombreRequest;
import com.accenture.franquicias.aplicacion.dto.FranquiciaRequest;
import com.accenture.franquicias.aplicacion.dto.FranquiciaResponse;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Franquicia;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FranquiciaService {
    
    private final FranquiciaRepositoryR2dbc repository;
    
    public Mono<FranquiciaResponse> crear(FranquiciaRequest request) {
        return repository.existsByNombre(request.getNombre())
            .flatMap(existe -> {
                if (existe > 0) {
                    return Mono.<Franquicia>error(new NombreDuplicadoException("Franquicia", request.getNombre()));
                }
                
                Franquicia franquicia = Franquicia.builder()
                    .nombre(request.getNombre())
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(LocalDateTime.now())
                    .build();
                
                return repository.save(franquicia);
            })
            .map(this::toResponse);
    }
    
    public Mono<FranquiciaResponse> actualizarNombre(Long id, ActualizarNombreRequest request) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Franquicia", id)))
            .flatMap(franquicia -> 
                repository.existsByNombreAndIdNot(request.getNombre(), id)
                    .flatMap(existe -> {
                        if (existe > 0) {
                            return Mono.<Franquicia>error(new NombreDuplicadoException("Franquicia", request.getNombre()));
                        }
                        
                        franquicia.setNombre(request.getNombre());
                        franquicia.setFechaActualizacion(LocalDateTime.now());
                        return repository.save(franquicia);
                    })
            )
            .map(this::toResponse);
    }
    
    public Flux<FranquiciaResponse> listarTodas() {
        return repository.findAll()
            .map(this::toResponse);
    }
    
    public Mono<FranquiciaResponse> buscarPorId(Long id) {
        return repository.findById(id)
            .map(this::toResponse)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Franquicia", id)));
    }
    
    private FranquiciaResponse toResponse(Franquicia franquicia) {
        return FranquiciaResponse.builder()
            .id(franquicia.getId())
            .nombre(franquicia.getNombre())
            .fechaCreacion(franquicia.getFechaCreacion())
            .fechaActualizacion(franquicia.getFechaActualizacion())
            .build();
    }
}
