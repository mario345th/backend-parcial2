package com.backend.parcial2.demo.persistence;
import com.backend.parcial2.demo.dto.ReporteHorasDTO;
import com.backend.parcial2.demo.entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEmpleadoCarnet(String carnet);

    @Query(value = "SELECT carnet_empleado AS carnet, " +
            "CAST(DATE(fecha_hora) AS VARCHAR) AS periodo, " +
            "TO_CHAR(MIN(fecha_hora), 'HH24:MI:SS') AS horaEntrada, " +
            "TO_CHAR(MAX(fecha_hora), 'HH24:MI:SS') AS horaSalida, " +
            "ROUND(CAST(EXTRACT(EPOCH FROM (MAX(fecha_hora) - MIN(fecha_hora))) / 3600.0 AS NUMERIC), 2) AS horasTrabajadas " +
            "FROM asistencias " +
            "GROUP BY carnet_empleado, DATE(fecha_hora) " +
            "ORDER BY DATE(fecha_hora) DESC, carnet_empleado",
            nativeQuery = true)
    List<ReporteHorasDTO> obtenerHorasTrabajadasPorDia();

    // 🌟 REPORTE MENSUAL (Las horas de entrada/salida devuelven '-')
    @Query(value = "SELECT carnet, " +
            "TO_CHAR(fecha, 'YYYY-MM') AS periodo, " +
            "'-' AS horaEntrada, " +
            "'-' AS horaSalida, " +
            "ROUND(SUM(horas_diarias), 2) AS horasTrabajadas " +
            "FROM (" +
            "   SELECT carnet_empleado AS carnet, " +
            "          DATE(fecha_hora) AS fecha, " +
            "          CAST(EXTRACT(EPOCH FROM (MAX(fecha_hora) - MIN(fecha_hora))) / 3600.0 AS NUMERIC) AS horas_diarias " +
            "   FROM asistencias " +
            "   GROUP BY carnet_empleado, DATE(fecha_hora)" +
            ") subconsulta " +
            "GROUP BY carnet, TO_CHAR(fecha, 'YYYY-MM') " +
            "ORDER BY TO_CHAR(fecha, 'YYYY-MM') DESC, carnet",
            nativeQuery = true)
    List<ReporteHorasDTO> obtenerHorasTrabajadasPorMes();
}