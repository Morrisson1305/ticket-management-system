package com.we2stars.ticket_desk.dto;

import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.enums.TicketPriority;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketDTO {
    private String title;
    private String description;
    private TicketStatus status;
    private AppUser assignedTo;
    private TicketPriority priority;
    private int score;
}
