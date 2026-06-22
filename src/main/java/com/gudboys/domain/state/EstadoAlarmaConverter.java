package com.gudboys.domain.state;

import com.gudboys.domain.enums.EstadoAlarma;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter   // jakarta.persistence.Converter
public class EstadoAlarmaConverter implements AttributeConverter<IEstadoAlarma, String> {
    
    @Override
    public String convertToDatabaseColumn(IEstadoAlarma estado) {
        return estado.getTipo().name();        // objeto -> "ACTIVA"/"ATENDIDA"/"FINALIZADA"
    }

    @Override
    public IEstadoAlarma convertToEntityAttribute(String dbValue) {
        return switch (EstadoAlarma.valueOf(dbValue)) {
            case ACTIVA     -> new EstadoActiva();
            case ATENDIDA   -> new EstadoAtendida();
            case FINALIZADA -> new EstadoFinalizada();
        };
    }
}

