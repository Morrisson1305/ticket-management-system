package com.we2stars.ticket_desk.service.impl;

import com.we2stars.ticket_desk.mailer.EmailService;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import com.we2stars.ticket_desk.model.enums.UserType;
import com.we2stars.ticket_desk.repo.TicketRepository;
import com.we2stars.ticket_desk.repo.UserRepository;
import com.we2stars.ticket_desk.service.TicketService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketSchedulerService {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void checkTickets() {
        List<Tickets> openTickets = ticketRepository.findAll().stream()
                .filter(ticket -> TicketStatus.OPEN.equals(ticket.getStatus()))
                .toList();

        for (Tickets ticket : openTickets) {
            LocalDateTime assignedTime = ticket.getAssignedTime();
            Duration completionTime = ticket.getPriority().getCompletionTime();
            LocalDateTime deadline = assignedTime.plus(completionTime);
            Duration duration = Duration.between(LocalDateTime.now(), deadline);

            if (duration.toMinutes() == 30 && duration.toSecondsPart() == 0) {
                emailService.sendReminderEmailToAllUsers(
                        ticket.getAssignedTo().getEmail(),
                        ticket.getId().toString(),
                        ticket.getTitle()
                );
            }
            if (duration.isNegative()) {
                ticket.setStatus(TicketStatus.OVERDUE);
                ticketRepository.save(ticket);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void generateAndSendWeeklyReport() throws MessagingException, IOException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusWeeks(1).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime endDate = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));

        ByteArrayInputStream report = ticketService.generateExcelReport(startDate, endDate);
        emailService.sendEmailWithAttachment("general.manager@example.com", "Weekly Noc Report", "Please find attached the weekly Noc ticket report.", report);
    }


    @Scheduled(fixedRate = 60000)
    public void reassignOverdueTicketsAutomatically() {
        List<Tickets> overdueTickets = ticketRepository.findByStatus(TicketStatus.OVERDUE);
        List<AppUser> users = userRepository.findAll();

        AppUser highestScoringUser = users.stream()
                .filter(user -> user.getType() == UserType.USER)
                .max(Comparator.comparingInt(AppUser::getScore))
                .orElse(null);

        if (highestScoringUser != null) {
            for (Tickets ticket : overdueTickets) {
                if (!ticket.getAssignedTo().equals(highestScoringUser)) {
                    ticket.setAssignedTo(highestScoringUser);
                    ticketRepository.save(ticket);
                    emailService.sendAssignmentEmail(highestScoringUser.getEmail(), ticket.getId().toString(), ticket.getTitle());
                }
            }
        }
    }

}

