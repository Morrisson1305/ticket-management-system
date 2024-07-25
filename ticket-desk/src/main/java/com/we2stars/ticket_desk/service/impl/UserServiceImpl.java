package com.we2stars.ticket_desk.service.impl;

import com.we2stars.ticket_desk.dto.UserScoreDTO;
import com.we2stars.ticket_desk.execeptions.EmailException;
import com.we2stars.ticket_desk.execeptions.PasswordException;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.Tickets;
import com.we2stars.ticket_desk.model.enums.TicketStatus;
import com.we2stars.ticket_desk.model.enums.UserType;
import com.we2stars.ticket_desk.repo.TicketRepository;
import com.we2stars.ticket_desk.repo.UserRepository;
import com.we2stars.ticket_desk.service.UserService;
import com.we2stars.ticket_desk.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final AppUtils appUtils;
    @Override
    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<AppUser> findByScore(int score) {
        return userRepository.findByScore(score);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public String register(AppUser user, String password2) {
        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            throw new PasswordException("Passwords do not match.");
        }
        Optional<AppUser> userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb.isPresent()) {
            throw new EmailException("Email is already used.");
        }
        user.setType(UserType.USER);
        user.setPassword(user.getPassword());
        userRepository.save(user);
        //Todo send registration email to users.
        return "User successfully registered.";
    }

    @Override
    public List<UserScoreDTO> getAllUserScores() {
            return userRepository.findAll().stream()
                    .map(user -> new UserScoreDTO(user.getEmail(), user.getName(), user.getScore()))
                    .collect(Collectors.toList());

    }

    @Override
    public List<UserScoreDTO> getUserScoresWithinDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Tickets> ticketsWithinRange = ticketRepository.findByClosedTimeBetween(startDateTime, endDateTime);

        return userRepository.findAll().stream()
                .map(user -> {
                    int score = ticketsWithinRange.stream()
                            .filter(ticket -> ticket.getAssignedTo().getId().equals(user.getId()) && ticket.getStatus() == TicketStatus.CLOSED)
                            .mapToInt(ticket -> appUtils.calculateScoreForTicket(ticket.getPriority()))
                            .sum();
                    return new UserScoreDTO(user.getEmail(), user.getName(), score);
                })
                .collect(Collectors.toList());
    }

}
