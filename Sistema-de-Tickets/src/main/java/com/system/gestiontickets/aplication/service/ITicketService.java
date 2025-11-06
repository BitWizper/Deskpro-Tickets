package com.system.gestiontickets.aplication.service;


import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;

import java.util.List;

public interface ITicketService {
    Ticket crearTicket(Ticket ticket);
    Ticket actualizarEstadoTicket(Long ticketId, EstadoTicket nuevoEstado);
    void eliminarTicket(Long id);
    Ticket obtenerTicketId(Long id);
    List<Ticket> obtenerTickets();
    List<Ticket> obtenerTicketsAbiertos();
    List<Ticket> obtenerTicketsEnProceso();
    List<Ticket> obtenerTicketsCerrados();

}
