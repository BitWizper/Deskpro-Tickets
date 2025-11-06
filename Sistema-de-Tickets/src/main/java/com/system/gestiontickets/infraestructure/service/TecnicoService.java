package com.system.gestiontickets.infraestructure.service; 

import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import com.system.gestiontickets.domain.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TicketRepository ticketRepository;

    // Asignar técnico a un ticket
    public Optional<Ticket> asignarTecnico(Long ticketId, String tecnico) {
        Ticket ticket = ticketRepository.obtenerTicketId(ticketId);
        if (ticket != null) {
            ticket.setTecnicoAsignado(tecnico);
            // Si el ticket está ABIERTO, cambiar a EN_PROCESO al asignar técnico
            if (ticket.getEstado() == EstadoTicket.ABIERTO) {
                ticket.setEstado(EstadoTicket.EN_PROCESO);
            }
            ticketRepository.actualizarEstadoTicket(ticket);
            return Optional.of(ticket);
        }
        return Optional.empty();
    }

    // Reasignar técnico a un ticket
    public Optional<Ticket> reasignarTecnico(Long ticketId, String nuevoTecnico) {
        Ticket ticket = ticketRepository.obtenerTicketId(ticketId);
        if (ticket != null) {
            ticket.setTecnicoAsignado(nuevoTecnico);
            ticketRepository.actualizarEstadoTicket(ticket);
            return Optional.of(ticket);
        }
        return Optional.empty();
    }

    // Obtener tickets asignados a un técnico específico
    public List<Ticket> obtenerTicketsPorTecnico(String tecnico) {
        List<Ticket> todosTickets = ticketRepository.obtenerTodosTickets();
        List<Ticket> ticketsDelTecnico = new ArrayList<>();
        
        for (Ticket ticket : todosTickets) {
            if (tecnico != null && tecnico.equals(ticket.getTecnicoAsignado())) {
                ticketsDelTecnico.add(ticket);
            }
        }
        return ticketsDelTecnico;
    }

    // Obtener tickets sin técnico asignado
    public List<Ticket> obtenerTicketsSinAsignar() {
        List<Ticket> todosTickets = ticketRepository.obtenerTodosTickets();
        List<Ticket> ticketsSinAsignar = new ArrayList<>();
        
        for (Ticket ticket : todosTickets) {
            if (ticket.getTecnicoAsignado() == null || ticket.getTecnicoAsignado().isEmpty()) {
                ticketsSinAsignar.add(ticket);
            }
        }
        return ticketsSinAsignar;
    }

    // Remover técnico de un ticket
    public Optional<Ticket> removerTecnico(Long ticketId) {
        Ticket ticket = ticketRepository.obtenerTicketId(ticketId);
        if (ticket != null) {
            ticket.setTecnicoAsignado(null);
            // Si el ticket estaba EN_PROCESO, cambiar a ABIERTO
            if (ticket.getEstado() == EstadoTicket.EN_PROCESO) {
                ticket.setEstado(EstadoTicket.ABIERTO);
            }
            ticketRepository.actualizarEstadoTicket(ticket);
            return Optional.of(ticket);
        }
        return Optional.empty();
    }

    // Obtener lista de técnicos que tienen tickets asignados
    public List<String> obtenerTecnicosActivos() {
        List<Ticket> todosTickets = ticketRepository.obtenerTodosTickets();
        List<String> tecnicos = new ArrayList<>();
        
        for (Ticket ticket : todosTickets) {
            String tecnico = ticket.getTecnicoAsignado();
            if (tecnico != null && !tecnico.isEmpty() && !tecnicos.contains(tecnico)) {
                tecnicos.add(tecnico);
            }
        }
        return tecnicos;
    }
}