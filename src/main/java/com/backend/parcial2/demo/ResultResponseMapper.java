package com.backend.parcial2.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResultResponseMapper {

    public static <T> ResponseEntity<?> toResponse(Result<T> result) {

        if (result.isSuccess()) {
            if (result.getValue() == null) {
                return ResponseEntity.ok(Map.of("message", "Operation completed successfully"));
            }
            return ResponseEntity.ok(result.getValue());
        }

        final String key = "message";

        return switch (result.getError()) {
            case INVALID_ID ->
                    ResponseEntity.badRequest()
                            .body(Map.of(key, "Identifier must be greater than zero"));
            case NOT_FOUND_BY_ID ->
                    ResponseEntity.badRequest()
                            .body(Map.of(key,  result.getClazz().getSimpleName() +" not found by id"));
            case ALREADY_EXIST ->
                    ResponseEntity.badRequest()
                            .body(Map.of(key, result.getClazz().getSimpleName() +" already exists with the same id"));
            case NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of(
                                    key,
                                    result.getClazz().getSimpleName() + " not found"
                            ));
            default ->
                    ResponseEntity.internalServerError()
                            .body(Map.of(key, "Unknown error"));
        };
    }
}