package com.backend.parcial2.demo;

import com.backend.parcial2.demo.Result;

import java.util.List;

public interface GenericService<T> {
    Result<List<T>> findAll();
    Result<T> findById(Long id);
    Result<T> save(T entity);
    Result<T> deleteById(Long id);
}
