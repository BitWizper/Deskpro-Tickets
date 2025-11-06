package com.system.gestiontickets.infraestructure.web.controller;

import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import com.system.gestiontickets.domain.model.entity.enums.PrioridadTicket;
import com.system.gestiontickets.infraestructure.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/por-estado")
    public ResponseEntity<Map<EstadoTicket, Long>> reportePorEstado() {
        Map<EstadoTicket, Long> reporte = reporteService.generarReportePorEstado();
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/por-prioridad")
    public ResponseEntity<Map<PrioridadTicket, Long>> reportePorPrioridad() {
        Map<PrioridadTicket, Long> reporte = reporteService.generarReportePorPrioridad();
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/por-tecnico")
    public ResponseEntity<Map<String, Long>> reportePorTecnico() {
        Map<String, Long> reporte = reporteService.generarReportePorTecnico();
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/combinado")
    public ResponseEntity<Map<EstadoTicket, Map<PrioridadTicket, Long>>> reporteCombinado() {
        Map<EstadoTicket, Map<PrioridadTicket, Long>> reporte = reporteService.generarReporteCombinado();
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<List<Ticket>> ticketsPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Ticket> tickets = reporteService.obtenerTicketsPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/metricas")
    public ResponseEntity<Map<String, Object>> metricasGenerales() {
        Map<String, Object> metricas = reporteService.obtenerMetricasGenerales();
        return ResponseEntity.ok(metricas);
    }
}