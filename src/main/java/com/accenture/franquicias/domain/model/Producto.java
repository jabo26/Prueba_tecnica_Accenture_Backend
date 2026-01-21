package com.accenture.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("productos")
public class Producto {
    
    @Id
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}