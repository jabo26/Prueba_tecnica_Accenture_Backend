package com.accenture.franquicias.domain.exception;

public class NombreDuplicadoException extends DominioException {
    
    public NombreDuplicadoException(String entidad, String nombre, String contexto) {
        super(String.format("%s con nombre '%s' ya existe en %s", entidad, nombre, contexto));
    }
    
    public NombreDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
