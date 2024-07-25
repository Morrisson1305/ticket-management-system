package com.we2stars.ticket_desk.mailer;

import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.enums.UserType;
import com.we2stars.ticket_desk.repo.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    private final UserRepository userRepository;


    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendTicketClosedEmailToManager( String managerEmail, String ticketId, String ticketTitle) {
        AppUser manager = userRepository.findByType(UserType.TICKET_MANAGER).stream().findFirst()
                .orElseThrow(()-> new RuntimeException("Ticket manager not found"));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(managerEmail);
        message.setSubject("Ticket Closed: "+ ticketTitle);
        message.setText("The ticket with ID " + ticketId + " and title '" + ticketTitle + "' has been closed" +  ".");

    }

    public void sendReminderEmailToAllUsers(String emails, String ticketId, String ticketTitle) {
        String subject = "Ticket Nearing Deadline: " + ticketId;
        String text = "A ticket with ID " + ticketId + " is nearing its deadline. Please review the ticket and take necessary action.";
        sendEmail(emails, subject, text);
    }

    public void sendAssignmentEmail(String email, String ticketId, String title) {
        String subject = "New Ticket Assigned: " + title;
        String text = "You have been assigned a new ticket with ID " + ticketId + " and title \"" + title + "\".";
        sendEmail(email, subject, text);
    }

    public void sendEmailWithAttachment(String to, String subject, String text, ByteArrayInputStream attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment("user_report.pdf", new ByteArrayResource(attachment.readAllBytes()));

        mailSender.send(message);
    }
}
