package com.we2stars.ticket_desk.service.impl;


import com.we2stars.ticket_desk.dto.TicketDTO;
import com.we2stars.ticket_desk.mailer.EmailService;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import com.we2stars.ticket_desk.model.enums.UserType;
import com.we2stars.ticket_desk.repo.TicketRepository;
import com.we2stars.ticket_desk.repo.UserRepository;
import com.we2stars.ticket_desk.service.TicketService;
import com.we2stars.ticket_desk.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {


    private final EmailService emailService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AppUtils appUtils;

    @Override
    public List<Tickets> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Tickets createTicket(TicketDTO ticketDTO) {
        AppUser user = userRepository.findById(ticketDTO.getAssignedTo().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Tickets ticket = new Tickets(ticketDTO.getTitle(), ticketDTO.getDescription(), TicketStatus.OPEN,
                user, ticketDTO.getPriority(), LocalDateTime.now());
        Tickets savedTicket = ticketRepository.save(ticket);
        emailService.sendAssignmentEmail(user.getEmail(), ticket.getId().toString(), ticket.getTitle());

        return savedTicket;
    }

    @Override
    public List<Tickets> getTicketsByUser(AppUser user) {
            if (UserType.USER.equals(user.getType())) {
                return ticketRepository.findAll();
            } else {
                return ticketRepository.findByAssignedTo(user);
            }

    }

    @Override
    public Optional<Tickets> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }


    @Override
    public void updateUserTicketStatus(Long ticketId, Long userId, TicketStatus status) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (ticket.getAssignedTo().getId().equals(userId)) {
            ticket.setStatus(status);
            if (status == TicketStatus.CLOSED) {
                ticket.setClosedTime(LocalDateTime.now());

                int score = appUtils.calculateScoreForTicket(ticket.getPriority());
                AppUser user = ticket.getAssignedTo();
                user.setScore(user.getScore() + score);

                userRepository.save(user);
                AppUser manager = userRepository.findByType(UserType.TICKET_MANAGER).stream().findFirst()
                        .orElseThrow(() -> new RuntimeException("Ticket manager not found"));
                emailService.sendTicketClosedEmailToManager(manager.getEmail(), ticket.getId().toString(), ticket.getTitle());
            }
            ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("User not authorized to update this ticket");
        }
    }


    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public List<Tickets> getTicketsByUserIdAndStatus(Long userId, TicketStatus status) {
        return ticketRepository.findByAssignedToIdAndStatus(userId, status);
    }


    @Override
    public ByteArrayInputStream generateExcelReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<AppUser> users = userRepository.findAll();
        List<Tickets> tickets = ticketRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("User Report");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Email");
        header.createCell(2).setCellValue("Tickets Closed");
        header.createCell(3).setCellValue("Overdue Tickets");
        header.createCell(4).setCellValue("In Progress Tickets");
        header.createCell(5).setCellValue("Points");

        int rowIdx = 1;
        for (AppUser user : users) {
            int ticketsClosed = appUtils.getTicketCount(tickets, user, TicketStatus.CLOSED, startDate, endDate);
            int overdueTickets = appUtils.getOverdueTicketCount(tickets, user, startDate, endDate);
            int inProgressTickets = appUtils.getTicketCount(tickets, user, TicketStatus.IN_PROGRESS, startDate, endDate);
            int points = user.getScore();

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getName());
            row.createCell(1).setCellValue(user.getEmail());
            row.createCell(2).setCellValue(ticketsClosed);
            row.createCell(3).setCellValue(overdueTickets);
            row.createCell(4).setCellValue(inProgressTickets);
            row.createCell(5).setCellValue(points);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

}
