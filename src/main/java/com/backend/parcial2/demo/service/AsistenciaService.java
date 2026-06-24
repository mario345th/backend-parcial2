package com.backend.parcial2.demo.service;

import com.backend.parcial2.demo.GenericServiceImpl;
import com.backend.parcial2.demo.LogicError;
import com.backend.parcial2.demo.Result;
import com.backend.parcial2.demo.entities.Asistencia;
import com.backend.parcial2.demo.entities.Empleado;
import com.backend.parcial2.demo.persistence.AsistenciaRepository;
import com.backend.parcial2.demo.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsistenciaService extends GenericServiceImpl<Asistencia> {

    private final AsistenciaRepository asistenciaRepository;
    private final EmpleadoRepository empleadoRepository;

    protected AsistenciaService(AsistenciaRepository asistenciaRepository, EmpleadoRepository empleadoRepository) {
        super(asistenciaRepository, Asistencia.class);
        this.asistenciaRepository = asistenciaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // Método principal para registrar asistencia vía QR
    public Result<Asistencia> registrarAsistencia(String carnetEmpleado) {
        Empleado empleadoReal = empleadoRepository.findById(carnetEmpleado).orElse(null);

        if (empleadoReal == null) {
            return Result.failure(LogicError.NOT_FOUND_BY_ID, Empleado.class);
        }

        Asistencia nuevaAsistencia = new Asistencia();
        nuevaAsistencia.setEmpleado(empleadoReal);
        nuevaAsistencia.setFechaHora(LocalDateTime.now());

        return Result.success(asistenciaRepository.save(nuevaAsistencia));
    }

    public Result<List<Asistencia>> obtenerAsistenciasPorEmpleado(String carnet) {
        List<Asistencia> asistencias = asistenciaRepository.findByEmpleadoCarnet(carnet);
        if (asistencias.isEmpty()) {
            return Result.failure(LogicError.NOT_FOUND, Asistencia.class);
        }
        return Result.success(asistencias);
    }
}