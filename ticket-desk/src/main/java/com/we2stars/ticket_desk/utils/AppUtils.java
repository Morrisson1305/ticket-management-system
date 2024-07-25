package com.we2stars.ticket_desk.utils;


import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketPriority;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class AppUtils {


    public int calculateScoreForTicket(TicketPriority priority) {
        return switch (priority) {
            case A -> 10;
            case B -> 7;
            case C -> 5;
        };
    }

    public int getTicketCount(List<Tickets> tickets, AppUser user, TicketStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return (int) tickets.stream()
                .filter(ticket -> ticket.getAssignedTo().equals(user) &&
                        ticket.getStatus() == status &&
                        ticket.getClosedTime() != null &&
                        ticket.getClosedTime().isAfter(startDate) &&
                        ticket.getClosedTime().isBefore(endDate))
                .count();
    }

    public int getOverdueTicketCount(List<Tickets> tickets, AppUser user, LocalDateTime startDate, LocalDateTime endDate) {
        return (int) tickets.stream()
                .filter(ticket -> ticket.getAssignedTo().equals(user) &&
                        ticket.getStatus() != TicketStatus.CLOSED &&
                        ticket.getDueDate() != null &&
                        ticket.getDueDate().isBefore(LocalDateTime.now()))
                .count();
    }
}