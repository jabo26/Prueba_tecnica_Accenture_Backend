package com.accenture.franquicias.aplicacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoStockResponse {
    
    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private Long sucursalId;
    private String nombreSucursal;
}