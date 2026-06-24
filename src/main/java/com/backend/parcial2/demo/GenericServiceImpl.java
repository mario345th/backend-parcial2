package com.backend.parcial2.demo;

import com.backend.parcial2.demo.LogicError;
import com.backend.parcial2.demo.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class GenericServiceImpl<T>  implements GenericService<T> {

    protected final JpaRepository<T, Long> repository;
    protected final Class<T> clazz;

    protected GenericServiceImpl(JpaRepository<T, Long> repository, Class<T> clazz) {
        this.repository = repository;
        this.clazz = clazz;
    }

    @Override
    public Result<List<T>> findAll() {
        List<T> list = repository.findAll();

        if (list.isEmpty()) {
            return Result.failure(LogicError.NOT_FOUND, clazz);
        }

        return Result.success(list);
    }

    @Override
    public Result<T> findById(Long id) {
        if (id == null || id <= 0) {
            return Result.failure(LogicError.INVALID_ID, clazz);
        }

        return repository.findById(id)
                .map(Result::success)
                .orElse(Result.failure(LogicError.NOT_FOUND, clazz));
    }

    @Override
    public Result<T> save(T entity) {
        return Result.success(repository.save(entity));
    }

    @Override
    public Result<T> deleteById(Long id) {
        if (id == null || id <= 0) {
            return Result.failure(LogicError.INVALID_ID, clazz);
        }

        boolean exists = repository.existsById(id);
        if (!exists) {
            return Result.failure(LogicError.NOT_FOUND, clazz);
        }

        try {
            repository.deleteById(id);
            return Result.success((T) id);
        } catch (Exception e) {
            // Opcional: Manejo de excepciones de persistencia (ej. restricción de clave foránea)
            return Result.failure(LogicError.DATABASE_ERROR, clazz);
        }
    }
}
