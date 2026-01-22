package com.accenture.franquicias.domain.exception;

public class NombreDuplicadoException extends DominioException {
    
    public NombreDuplicadoException(String entidad, String nombre) {
        super(String.format("%s con nombre '%s' ya existe", entidad, nombre));
    }
    
    public NombreDuplicadoException(String mensaje) {
        super(mensaje);
    }
}