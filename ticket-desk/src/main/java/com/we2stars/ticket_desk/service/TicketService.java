package com.we2stars.ticket_desk.service;

import com.we2stars.ticket_desk.dto.TicketDTO;
import com.we2stars.ticket_desk.dto.UserScoreDTO;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Tickets> getAllTickets();

    Tickets createTicket(TicketDTO ticketDTO);

    List<Tickets> getTicketsByUser(AppUser user);

    Optional<Tickets> getTicketById(Long id);

    void updateUserTicketStatus(Long ticketId, Long userId, TicketStatus status);

    void deleteTicket(Long id);

    List<Tickets> getTicketsByUserIdAndStatus(Long userId, TicketStatus status);

    ByteArrayInputStream generateExcelReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException;


}

