package com.accenture.franquicias.aplicacion.service;

import com.accenture.franquicias.aplicacion.dto.ActualizarNombreRequest;
import com.accenture.franquicias.aplicacion.dto.SucursalRequest;
import com.accenture.franquicias.aplicacion.dto.SucursalResponse;
import com.accenture.franquicias.domain.exception.EntidadNoEncontradaException;
import com.accenture.franquicias.domain.exception.NombreDuplicadoException;
import com.accenture.franquicias.domain.model.Sucursal;
import com.accenture.franquicias.infraestructura.repository.FranquiciaRepositoryR2dbc;
import com.accenture.franquicias.infraestructura.repository.SucursalRepositoryR2dbc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SucursalService {
    
    private final SucursalRepositoryR2dbc repository;
    private final FranquiciaRepositoryR2dbc franquiciaRepository;
    
    public Mono<SucursalResponse> crear(SucursalRequest request) {
        return franquiciaRepository.findById(request.getFranquiciaId())
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Franquicia", request.getFranquiciaId())))
            .flatMap(franquicia -> 
                repository.existsByNombreAndFranquiciaId(request.getNombre(), request.getFranquiciaId())
                    .flatMap(existe -> {
                        if (Boolean.TRUE.equals(existe)) {
                            return Mono.<Sucursal>error(new NombreDuplicadoException("Sucursal", request.getNombre()));
                        }
                        
                        Sucursal sucursal = Sucursal.builder()
                            .nombre(request.getNombre())
                            .franquiciaId(request.getFranquiciaId())
                            .fechaCreacion(LocalDateTime.now())
                            .fechaActualizacion(LocalDateTime.now())
                            .build();
                        
                        return repository.save(sucursal);
                    })
            )
            .map(this::toResponse);
    }
    
    public Mono<SucursalResponse> actualizarNombre(Long id, ActualizarNombreRequest request) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Sucursal", id)))
            .flatMap(sucursal -> 
                repository.existsByNombreAndFranquiciaIdAndIdNot(request.getNombre(), sucursal.getFranquiciaId(), id)
                    .flatMap(existe -> {
                        if (Boolean.TRUE.equals(existe)) {
                            return Mono.<Sucursal>error(new NombreDuplicadoException("Sucursal", request.getNombre()));
                        }
                        
                        sucursal.setNombre(request.getNombre());
                        sucursal.setFechaActualizacion(LocalDateTime.now());
                        return repository.save(sucursal);
                    })
            )
            .map(this::toResponse);
    }
    
    public Flux<SucursalResponse> listarPorFranquicia(Long franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Franquicia", franquiciaId)))
            .flatMapMany(f -> repository.findByFranquiciaId(franquiciaId))
            .map(this::toResponse);
    }
    
    public Flux<SucursalResponse> listarTodas() {
        return repository.findAll()
            .map(this::toResponse);
    }
    
    public Mono<SucursalResponse> buscarPorId(Long id) {
        return repository.findById(id)
            .map(this::toResponse)
            .switchIfEmpty(Mono.error(new EntidadNoEncontradaException("Sucursal", id)));
    }
    
    private SucursalResponse toResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
            .id(sucursal.getId())
            .nombre(sucursal.getNombre())
            .franquiciaId(sucursal.getFranquiciaId())
            .fechaCreacion(sucursal.getFechaCreacion())
            .fechaActualizacion(sucursal.getFechaActualizacion())
            .build();
    }
}
