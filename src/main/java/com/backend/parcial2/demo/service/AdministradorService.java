package com.backend.parcial2.demo.service;

import com.backend.parcial2.demo.GenericServiceImpl;
import com.backend.parcial2.demo.LogicError;
import com.backend.parcial2.demo.Result;
import com.backend.parcial2.demo.entities.Administrador;
import com.backend.parcial2.demo.persistence.AdministradorRepository;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService extends GenericServiceImpl<Administrador> {

    private final AdministradorRepository administradorRepository;

    protected AdministradorService(AdministradorRepository administradorRepository) {
        super(administradorRepository, Administrador.class);
        this.administradorRepository = administradorRepository;
    }

    public Result<Administrador> login(String usuario, String password) {
        return administradorRepository.findByUsuario(usuario)
                .filter(admin -> admin.getPassword().equals(password)) // En prod, usa BCrypt o similar
                .map(Result::success)
                .orElse(Result.failure(LogicError.NOT_FOUND, Administrador.class));
    }
}