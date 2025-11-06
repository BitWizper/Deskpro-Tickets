package com.system.gestiontickets.infraestructure.web.controller;

import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.domain.model.entity.enums.EstadoTicket;
import com.system.gestiontickets.aplication.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = "http://localhost:5173") // ← SOLO AGREGAR ESTA LÍNEA
public class TicketController {

    @Autowired
    private ITicketService ticketService; // Inyección de dependencias del servicio instanciamos la interfaZ ITicketService

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.crearTicket(ticket));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> obtenerTickets() {
        return ResponseEntity.ok(ticketService.obtenerTickets());
    }

   @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerTicketId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.obtenerTicketId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoTicket(
            @PathVariable Long id,
            @RequestParam("estado") String estadoStr) {  // Recibe el estado como String
        try {
            EstadoTicket estado = EstadoTicket.valueOf(estadoStr.toUpperCase());
            Ticket ticketActualizado = ticketService.actualizarEstadoTicket(id, estado);
            return ResponseEntity.ok(ticketActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado no válido. Valores permitidos: " + Arrays.toString(EstadoTicket.values()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        ticketService.eliminarTicket(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/ticketsAbiertos")
    public ResponseEntity<List<Ticket>> obtenerTicketsAbiertos() {
        return ResponseEntity.ok(ticketService.obtenerTicketsAbiertos());
    }

    @GetMapping("/ticketsEnProceso")
    public ResponseEntity<List<Ticket>> obtenerTicketsEnProceso() {
        return ResponseEntity.ok(ticketService.obtenerTicketsEnProceso());
    }

    @GetMapping("/ticketsCerrados")
    public ResponseEntity<List<Ticket>> obtenerTicketsCerrados() {
        return ResponseEntity.ok(ticketService.obtenerTicketsCerrados());
    }
}