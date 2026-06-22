package com.gudboys.exceptions;

/**
 * Excepción de dominio para recursos no encontrados (→ HTTP 404).
 * Usar en lugar de RuntimeException genérica en los services.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String mensaje) {
        super(mensaje);
    }
}
