package com.gudboys.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gudboys.exceptions.animal.AnimalException;
import com.gudboys.exceptions.animal.AnimalNotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAnimalNotFound(AnimalNotFoundException err) {
        return buildResponse(HttpStatus.NOT_FOUND, err.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NotFoundException err) {
        return buildResponse(HttpStatus.NOT_FOUND, err.getMessage());
    }

    @ExceptionHandler(AnimalException.class)
    public ResponseEntity<ErrorResponseDTO> handleAnimal(AnimalException err) {
        return buildResponse(HttpStatus.BAD_REQUEST, err.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusiness(BusinessException err) {
        return buildResponse(HttpStatus.CONFLICT, err.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException err) {
        String mensaje = err.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception err) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, err.getMessage());
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message) {
        ErrorResponseDTO body = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
