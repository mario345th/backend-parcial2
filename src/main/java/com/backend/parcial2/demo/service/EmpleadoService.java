package com.backend.parcial2.demo.service;

import com.backend.parcial2.demo.LogicError;
import com.backend.parcial2.demo.Result;
import com.backend.parcial2.demo.entities.Empleado;
import com.backend.parcial2.demo.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Result<List<Empleado>> findAll() {
        List<Empleado> list = empleadoRepository.findAll();
        if (list.isEmpty()) {
            return Result.failure(LogicError.NOT_FOUND, Empleado.class);
        }
        return Result.success(list);
    }

    public Result<Empleado> findById(String carnet) {
        if (carnet == null || carnet.trim().isEmpty()) {
            return Result.failure(LogicError.INVALID_ID, Empleado.class);
        }
        return empleadoRepository.findById(carnet)
                .map(Result::success)
                .orElse(Result.failure(LogicError.NOT_FOUND, Empleado.class));
    }

    public Result<Empleado> save(Empleado empleado) {
        if (empleadoRepository.existsById(empleado.getCarnet())) {
            return Result.failure(LogicError.ALREADY_EXIST, Empleado.class);
        }
        return Result.success(empleadoRepository.save(empleado));
    }
}