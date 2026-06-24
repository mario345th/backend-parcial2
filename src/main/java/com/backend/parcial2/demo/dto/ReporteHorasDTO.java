package com.backend.parcial2.demo.dto;

public interface ReporteHorasDTO {
    String getCarnet();
    String getPeriodo();
    String getHoraEntrada();   // <-- NUEVO
    String getHoraSalida();    // <-- NUEVO
    Double getHorasTrabajadas();
}