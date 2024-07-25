package com.we2stars.ticket_desk.model.enums;

import lombok.Getter;

import java.time.Duration;

@Getter
public enum TicketPriority {
    A(Duration.ofMinutes(150)), // 2:30 hours
    B(Duration.ofMinutes(270)), // 4:30 hours
    C(Duration.ofMinutes(510)); // 8:30 hours

    private final Duration completionTime;

    TicketPriority(Duration completionTime) {
        this.completionTime = completionTime;
    }
}
