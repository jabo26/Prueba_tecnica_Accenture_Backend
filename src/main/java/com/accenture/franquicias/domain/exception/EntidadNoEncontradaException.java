package com.accenture.franquicias.domain.exception;

public class EntidadNoEncontradaException extends DominioException {
    
    public EntidadNoEncontradaException(String entidad, Long id) {
        super(String.format("%s con ID %d no encontrada", entidad, id));
    }
    
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
