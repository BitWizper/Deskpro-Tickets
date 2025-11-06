package com.system.gestiontickets.infraestructure.web.controller;

import com.system.gestiontickets.domain.model.entity.Ticket;
import com.system.gestiontickets.infraestructure.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "*")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    @PutMapping("/asignar/{ticketId}")
    public ResponseEntity<?> asignarTecnico(@PathVariable Long ticketId, @RequestParam String tecnico) {
        Optional<Ticket> ticket = tecnicoService.asignarTecnico(ticketId, tecnico);
        if (ticket.isPresent()) {
            return ResponseEntity.ok(ticket.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/reasignar/{ticketId}")
    public ResponseEntity<?> reasignarTecnico(@PathVariable Long ticketId, @RequestParam String nuevoTecnico) {
        Optional<Ticket> ticket = tecnicoService.reasignarTecnico(ticketId, nuevoTecnico);
        if (ticket.isPresent()) {
            return ResponseEntity.ok(ticket.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tecnico}/tickets")
    public ResponseEntity<List<Ticket>> obtenerTicketsPorTecnico(@PathVariable String tecnico) {
        List<Ticket> tickets = tecnicoService.obtenerTicketsPorTecnico(tecnico);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/sin-asignar")
    public ResponseEntity<List<Ticket>> obtenerTicketsSinAsignar() {
        List<Ticket> tickets = tecnicoService.obtenerTicketsSinAsignar();
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/remover/{ticketId}")
    public ResponseEntity<?> removerTecnico(@PathVariable Long ticketId) {
        Optional<Ticket> ticket = tecnicoService.removerTecnico(ticketId);
        if (ticket.isPresent()) {
            return ResponseEntity.ok(ticket.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<String>> obtenerTecnicosActivos() {
        List<String> tecnicos = tecnicoService.obtenerTecnicosActivos();
        return ResponseEntity.ok(tecnicos);
    }
}