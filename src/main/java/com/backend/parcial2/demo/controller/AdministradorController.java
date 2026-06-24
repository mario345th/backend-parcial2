package com.backend.parcial2.demo.controller;

import com.backend.parcial2.demo.ResultResponseMapper;
import com.backend.parcial2.demo.entities.Administrador;
import com.backend.parcial2.demo.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(originPatterns = "*", methods = {RequestMethod.POST, RequestMethod.GET})
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Administrador adminRequest) {
        return ResultResponseMapper.toResponse(
                administradorService.login(adminRequest.getUsuario(), adminRequest.getPassword())
        );
    }
}