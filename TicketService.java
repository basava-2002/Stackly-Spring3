package com.example.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Exception.DuplicateTicketException;
import com.example.Exception.TicketNotFoundException;
import com.example.Model.Ticket;

@Service
public class TicketService {

    private final List<Ticket> tickets = new ArrayList<>();

    // 1. Book a ticket.
    public String bookTicket(Ticket ticket) {
        boolean idExists = tickets.stream()
                .anyMatch(t -> t.getTicketId().equals(ticket.getTicketId()));
        if (idExists) {
            throw new DuplicateTicketException("Ticket ID already exists");
        }
        checkSeatFree(ticket, null);
        tickets.add(ticket);
        return "Ticket booked successfully";
    }

    // 2. Get all tickets.
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    // 3. Get a ticket by ID.
    public Ticket getTicketById(int ticketId) {
        return tickets.stream()
                .filter(t -> t.getTicketId() == ticketId)
                .findFirst()
                .orElseThrow(() -> new TicketNotFoundException(
                        "Ticket not found with ID: " + ticketId));
    }

    // 4. Update a ticket.
    public String updateTicket(int ticketId, Ticket updated) {
        Ticket existing = getTicketById(ticketId);
        checkSeatFree(updated, ticketId);
        existing.setMovieName(updated.getMovieName());
        existing.setCustomerName(updated.getCustomerName());
        existing.setSeatNumber(updated.getSeatNumber());
        existing.setTicketPrice(updated.getTicketPrice());
        return "Ticket updated successfully";
    }

    // 5. Cancel (delete) a ticket.
    public String cancelTicket(int ticketId) {
        Ticket ticket = getTicketById(ticketId);
        tickets.remove(ticket);
        return "Ticket cancelled successfully";
    }

    // 6. Search by movie name (case-insensitive, partial).
    public List<Ticket> searchByMovie(String movieName) {
        String search = movieName.trim().toLowerCase();
        return tickets.stream()
                .filter(t -> t.getMovieName() != null
                        && t.getMovieName().toLowerCase().contains(search))
                .toList();
    }

    // Same seat cannot be booked twice for the same movie.
    private void checkSeatFree(Ticket ticket, Integer ignoreId) {
        if (ticket.getSeatNumber() == null || ticket.getSeatNumber().isBlank()
                || ticket.getMovieName() == null) {
            return;
        }
        boolean taken = tickets.stream()
                .filter(t -> ignoreId == null || !t.getTicketId().equals(ignoreId))
                .anyMatch(t -> t.getMovieName().equalsIgnoreCase(ticket.getMovieName())
                        && t.getSeatNumber() != null
                        && t.getSeatNumber().equalsIgnoreCase(ticket.getSeatNumber()));
        if (taken) {
            throw new DuplicateTicketException("Seat already booked for this movie");
        }
    }
}