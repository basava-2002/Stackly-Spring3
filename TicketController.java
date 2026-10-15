package com.example.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.Model.Ticket;
import com.example.Service.TicketService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<String> bookTicket(@Valid @RequestBody Ticket ticket) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.bookTicket(ticket));
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{ticketId}")
    public Ticket getTicketById(@PathVariable @Positive int ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @PutMapping("/{ticketId}")
    public String updateTicket(@PathVariable @Positive int ticketId,
                               @Valid @RequestBody Ticket ticket) {
        return ticketService.updateTicket(ticketId, ticket);
    }

    @DeleteMapping("/{ticketId}")
    public String cancelTicket(@PathVariable @Positive int ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @GetMapping("/search")
    public List<Ticket> searchByMovie(@RequestParam String movieName) {
        return ticketService.searchByMovie(movieName);
    }
}