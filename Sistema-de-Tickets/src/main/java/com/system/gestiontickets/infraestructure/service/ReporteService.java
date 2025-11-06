package com.system.gestiontickets.infraestructure.service;

import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import com.system.gestiontickets.domain.model.entity.enums.PrioridadTicket;
import com.system.gestiontickets.domain.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private TicketRepository ticketRepository;

    // Reporte por estado
    public Map<EstadoTicket, Long> generarReportePorEstado() {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        Map<EstadoTicket, Long> reporte = new HashMap<>();
        
        for (Ticket ticket : todosLosTickets) {
            EstadoTicket estado = ticket.getEstado();
            reporte.put(estado, reporte.getOrDefault(estado, 0L) + 1);
        }
        return reporte;
    }

    // Reporte por prioridad
    public Map<PrioridadTicket, Long> generarReportePorPrioridad() {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        Map<PrioridadTicket, Long> reporte = new HashMap<>();
        
        for (Ticket ticket : todosLosTickets) {
            PrioridadTicket prioridad = ticket.getPrioridad();
            reporte.put(prioridad, reporte.getOrDefault(prioridad, 0L) + 1);
        }
        return reporte;
    }

    // Reporte por técnico
    public Map<String, Long> generarReportePorTecnico() {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        Map<String, Long> reporte = new HashMap<>();
        
        for (Ticket ticket : todosLosTickets) {
            String tecnico = ticket.getTecnicoAsignado();
            if (tecnico != null && !tecnico.isEmpty()) {
                reporte.put(tecnico, reporte.getOrDefault(tecnico, 0L) + 1);
            }
        }
        return reporte;
    }

    // Reporte combinado: estado y prioridad
    public Map<EstadoTicket, Map<PrioridadTicket, Long>> generarReporteCombinado() {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        Map<EstadoTicket, Map<PrioridadTicket, Long>> reporte = new HashMap<>();
        
        for (Ticket ticket : todosLosTickets) {
            EstadoTicket estado = ticket.getEstado();
            PrioridadTicket prioridad = ticket.getPrioridad();
            
            reporte.putIfAbsent(estado, new HashMap<>());
            Map<PrioridadTicket, Long> prioridades = reporte.get(estado);
            prioridades.put(prioridad, prioridades.getOrDefault(prioridad, 0L) + 1);
        }
        return reporte;
    }

    // Tickets por rango de fechas
    public List<Ticket> obtenerTicketsPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        List<Ticket> ticketsEnRango = new ArrayList<>();
        
        for (Ticket ticket : todosLosTickets) {
            LocalDateTime fechaCreacion = ticket.getFechaCreacion();
            if (fechaCreacion != null && 
                !fechaCreacion.isBefore(fechaInicio) && 
                !fechaCreacion.isAfter(fechaFin)) {
                ticketsEnRango.add(ticket);
            }
        }
        return ticketsEnRango;
    }

    // Métricas generales del sistema
    public Map<String, Object> obtenerMetricasGenerales() {
        List<Ticket> todosLosTickets = ticketRepository.obtenerTodosTickets();
        
        long totalTickets = todosLosTickets.size();
        long ticketsAbiertos = 0;
        long ticketsEnProceso = 0;
        long ticketsResueltos = 0;
        long ticketsCerrados = 0;
        long ticketsSinAsignar = 0;

        for (Ticket ticket : todosLosTickets) {
            EstadoTicket estado = ticket.getEstado();
            
            if (estado == EstadoTicket.ABIERTO) {
                ticketsAbiertos++;
            } else if (estado == EstadoTicket.EN_PROCESO) {
                ticketsEnProceso++;
            } else if (estado == EstadoTicket.RESUELTO) {
                ticketsResueltos++;
            } else if (estado == EstadoTicket.CERRADO) {
                ticketsCerrados++;
            }
            
            if (ticket.getTecnicoAsignado() == null || ticket.getTecnicoAsignado().isEmpty()) {
                ticketsSinAsignar++;
            }
        }

        Map<String, Object> metricas = new HashMap<>();
        metricas.put("totalTickets", totalTickets);
        metricas.put("ticketsAbiertos", ticketsAbiertos);
        metricas.put("ticketsEnProceso", ticketsEnProceso);
        metricas.put("ticketsResueltos", ticketsResueltos);
        metricas.put("ticketsCerrados", ticketsCerrados);
        metricas.put("ticketsSinAsignar", ticketsSinAsignar);
        metricas.put("ticketsAsignados", totalTickets - ticketsSinAsignar);

        return metricas;
    }
}