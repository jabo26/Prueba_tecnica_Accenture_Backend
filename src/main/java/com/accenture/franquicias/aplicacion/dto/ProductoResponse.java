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
public class ProductoResponse {
    
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}