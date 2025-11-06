package com.system.gestiontickets.domain.model.repository;

/*
Clase TicketRepository que gestiona los tickets
*
* */
import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TicketRepository {
    private Map<Long, Ticket> tickets = new HashMap<>();
    private Long id = 1L;

    // 1. Metodo para crear un ticket
    public Ticket crearTicket(Ticket ticket) {
        ticket.setId(id++);
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    //2. Metodo para obtener todos los tickets
    public List<Ticket> obtenerTodosTickets() {
        return new ArrayList<>(tickets.values());
    }

    //3. Metodo para obtener un ticket por id
    public Ticket obtenerTicketId(Long id) {
        return tickets.get(id);
    }

    //4. Metodo para actualizar un ticket
    public Ticket actualizarEstadoTicket(Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    //5. Metodo para eliminar un ticket
    public void eliminarTicket(Long id) {
        tickets.remove(id);
    }

    //6. Obtener todos los tickets que esten abiertos
    public List<Ticket> obtenerTicketsAbiertos() {
        List<Ticket> ticketsAbiertos = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getEstado() == EstadoTicket.ABIERTO) {
                ticketsAbiertos.add(ticket);
            }
        }
        return ticketsAbiertos;
    }

    //7. Obtener todos los tickets que esten en proceso
    public List<Ticket> obtenerTicketsEnProceso() {
        List<Ticket> ticketsEnProceso = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getEstado() == EstadoTicket.EN_PROCESO) {
                ticketsEnProceso.add(ticket);
            }
        }
        return ticketsEnProceso;
    }

    //8. Obtener todos los tickets que esten cerrados
    public List<Ticket> obtenerTicketsCerrados() {
        List<Ticket> ticketsCerrados = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getEstado() == EstadoTicket.CERRADO) {
                ticketsCerrados.add(ticket);
            }
        }
        return ticketsCerrados;
    }



}
