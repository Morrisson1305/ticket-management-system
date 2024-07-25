package com.we2stars.ticket_desk.repo;

import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets, Long> {

    List<Tickets> findByAssignedTo(AppUser user);
    List<Tickets> findByStatus(TicketStatus overdue);

    List<Tickets> findByAssignedToIdAndStatus(Long userId, TicketStatus status);
    List<Tickets> findByClosedTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
