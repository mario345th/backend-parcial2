package com.backend.parcial2.demo;

public class Result <T>{

    private final T value;
    private final LogicError error;
    private final Class<?> clazz;

    private Result(T value, LogicError error, Class<?> clazz) {
        this.value = value;
        this.error = error;
        this.clazz = clazz;
    }


    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, value.getClass());
    }
    public static <T> Result<T> success(T value, Class<?> clazz) {
        return new Result<>(value, null, clazz);
    }

    public static <T> Result<T> failure(LogicError error, Class<?> clazz) {
        return new Result<>(null, error, clazz);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot get value from failure result");
        }
        return value;
    }

    public LogicError getError() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error from success result");
        }
        return error;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}