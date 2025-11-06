package com.system.gestiontickets.infraestructure.service;

import com.system.gestiontickets.aplication.service.ITicketService;
import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import com.system.gestiontickets.domain.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TicketServiceImpl implements ITicketService {

    @Autowired
    private TicketRepository repository;

    @Override
    public Ticket crearTicket(Ticket ticket) {
        // Validación
        if (ticket.getTitulo() == null || ticket.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        // Asignar valores
        ticket.setTitulo(ticket.getTitulo().trim());
        ticket.setFechaCreacion(LocalDateTime.now()); // Guarda como LocalDateTime
        ticket.setEstado(EstadoTicket.ABIERTO);
        return repository.crearTicket(ticket); // Guarda el Ticket con LocalDateTime
    }

    @Override
    public Ticket actualizarEstadoTicket(Long ticketId, EstadoTicket nuevoEstado) {
        // 1. Validar que el nuevoEstado no sea nulo
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }

        // 2. Obtener el ticket (con manejo de excepción específica)
        Ticket ticket = repository.obtenerTicketId(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket con id " + ticketId + " no existe");
        }

        // 3. Validar transiciones de estado
        EstadoTicket estadoActual = ticket.getEstado();

        if (estadoActual == EstadoTicket.CERRADO) {
            throw new IllegalStateException("No se puede modificar un ticket ya CERRADO");
        }

        if (estadoActual == EstadoTicket.ABIERTO && nuevoEstado == EstadoTicket.CERRADO) {
            throw new IllegalStateException(
                    "No se puede cerrar un ticket directamente desde ABIERTO. Primero debe estar EN_PROCESO");
        }

        // 4. Actualizar estado y fecha de cierre (si aplica)
        ticket.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoTicket.CERRADO) {
            ticket.setFechaCierre(LocalDateTime.now());
        }

        // 5. Guardar y retornar
        return repository.actualizarEstadoTicket(ticket);
    }

    @Override
    public void eliminarTicket(Long id) {
        repository.eliminarTicket(id);
    }

    @Override
    public Ticket obtenerTicketId(Long id) {
        return repository.obtenerTicketId(id);
    }

    @Override

    public List<Ticket> obtenerTickets() {
        return repository.obtenerTodosTickets();
    }

    @Override
    public List<Ticket> obtenerTicketsAbiertos() {
        return repository.obtenerTicketsAbiertos();

    }

    @Override
    public List<Ticket> obtenerTicketsCerrados() {
        return repository.obtenerTicketsCerrados();

    }

    @Override
    public List<Ticket> obtenerTicketsEnProceso() {
        return repository.obtenerTicketsEnProceso();

    }
}
