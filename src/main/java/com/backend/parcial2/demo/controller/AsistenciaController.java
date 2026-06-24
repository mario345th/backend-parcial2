package com.backend.parcial2.demo.controller;

import com.backend.parcial2.demo.ResultResponseMapper;
import com.backend.parcial2.demo.service.AsistenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(originPatterns = "*", methods = {RequestMethod.POST, RequestMethod.GET})
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAsistencias() {
        return ResultResponseMapper.toResponse(asistenciaService.findAll());
    }

    @GetMapping("/empleado/{carnet}")
    public ResponseEntity<?> getAsistenciasDeEmpleado(@PathVariable String carnet) {
        return ResultResponseMapper.toResponse(asistenciaService.obtenerAsistenciasPorEmpleado(carnet));
    }

    // Endpoint clave para el escaneo del QR
    @PostMapping("/escanear")
    public ResponseEntity<?> registrarAsistenciaQR(@RequestBody Map<String, String> payload) {
        String carnet = payload.get("carnet");
        return ResultResponseMapper.toResponse(asistenciaService.registrarAsistencia(carnet));
    }

    @GetMapping("/reporte/diario")
    public ResponseEntity<?> getReporteDiario() {
        return ResultResponseMapper.toResponse(asistenciaService.obtenerReporteDiario());
    }

    @GetMapping("/reporte/mensual")
    public ResponseEntity<?> getReporteMensual() {
        return ResultResponseMapper.toResponse(asistenciaService.obtenerReporteMensual());
    }
}