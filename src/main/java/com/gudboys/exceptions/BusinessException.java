package com.gudboys.exceptions;

/**
 * Excepción de dominio para violaciones de reglas de negocio (→ HTTP 409 / 400).
 * Usar en lugar de RuntimeException genérica en los services
 * (p. ej. animal no adoptable, límite de adopciones, seguimiento duplicado).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
