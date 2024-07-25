package com.we2stars.ticket_desk.controller;

import com.we2stars.ticket_desk.dto.TicketDTO;
import com.we2stars.ticket_desk.dto.UserScoreDTO;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import com.we2stars.ticket_desk.service.TicketService;
import com.we2stars.ticket_desk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TicketController {

    private final TicketService ticketService;

    private final UserService userService;

    @GetMapping("/users/tickets")
    @PreAuthorize("hasRole('USER','TICKET_MANAGER')")
    public List<Tickets> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('TICKET_MANAGER')")
    public ResponseEntity<Tickets> getTicketById(@PathVariable Long id) {
        Optional<Tickets> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/user-ticket/{userId}")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public List<Tickets> getTicketsByUser(@PathVariable Long userId) {
        AppUser user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ticketService.getTicketsByUser(user);
    }

    @PostMapping("/admin/add-ticket")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public Tickets createTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.createTicket(ticketDTO);
    }


    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/status")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_TICKET_MANAGER)")
    public ResponseEntity<List<Tickets>> getTicketsByUserIdAndStatus(@PathVariable Long userId, @RequestParam TicketStatus status) {
        List<Tickets> tickets = ticketService.getTicketsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/user/{userId}/ticket/{ticketId}/status")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_TICKET_MANAGER)")
    public ResponseEntity<?> updateUserTicketStatus(@PathVariable Long userId, @PathVariable Long ticketId, @RequestParam TicketStatus status) {
        try {
            ticketService.updateUserTicketStatus(ticketId, userId, status);
            return ResponseEntity.ok("Ticket status updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

  }



