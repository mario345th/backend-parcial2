package com.backend.parcial2.demo.persistence;

import com.backend.parcial2.demo.entities.Administrador;
import com.backend.parcial2.demo.entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsuario(String usuario);
}