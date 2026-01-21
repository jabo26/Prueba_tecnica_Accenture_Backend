package com.accenture.franquicias.aplicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponse {
    
    private Long id;
    private String nombre;
    private Long franquiciaId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}