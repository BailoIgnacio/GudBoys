package com.gudboys.infrastructure.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceAdapter {

    @Value("${gudboys.auth.service-url}")
    private String authServiceUrl;

    public boolean validarToken(String token) {
        // TODO: implementar validacion de token con el modulo externo de autenticacion
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String obtenerExternalIdDesdeToken(String token) {
        // TODO: implementar extraccion de externalId desde el token del modulo de autenticacion
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
