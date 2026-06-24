package com.backend.parcial2.demo.controller;

import com.backend.parcial2.demo.ResultResponseMapper;
import com.backend.parcial2.demo.entities.Empleado;
import com.backend.parcial2.demo.service.EmpleadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
//@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET})
@CrossOrigin(originPatterns = "*", methods = {RequestMethod.POST, RequestMethod.GET})
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<?> getAllEmpleados() {
        return ResultResponseMapper.toResponse(empleadoService.findAll());
    }

    @GetMapping("/{carnet}")
    public ResponseEntity<?> getEmpleadoByCarnet(@PathVariable String carnet) {
        return ResultResponseMapper.toResponse(empleadoService.findById(carnet));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmpleado(@RequestBody Empleado empleado) {
        return ResultResponseMapper.toResponse(empleadoService.save(empleado));
    }
}