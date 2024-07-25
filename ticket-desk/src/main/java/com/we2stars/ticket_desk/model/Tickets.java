package com.we2stars.ticket_desk.model;

import com.we2stars.ticket_desk.model.enums.TicketPriority;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private AppUser assignedTo;
    @Enumerated(EnumType.STRING)
    private TicketPriority priority;
    private LocalDateTime assignedTime;
    private LocalDateTime createdTime;
    private LocalDateTime closedTime;
    private LocalDateTime dueDate;

    public Tickets(String title, String description, TicketStatus status, AppUser assignedTo, TicketPriority priority, LocalDateTime assignedTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
        this.priority = priority;
        this.assignedTime = assignedTime;
    }
}
