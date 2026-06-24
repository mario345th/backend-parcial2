package com.backend.parcial2.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Capturar errores específicos de integridad de Base de Datos (Llaves foráneas, etc.)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("error", "DATABASE_RESTRICTION");
        body.put("message", "No se puede eliminar o modificar el registro porque tiene dependencias activas.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // Código 409
    }

    // 2. Capturar CUALQUIER otra excepción inesperada del sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("error", "INTERNAL_SERVER_ERROR");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body); // Código 500
    }
}