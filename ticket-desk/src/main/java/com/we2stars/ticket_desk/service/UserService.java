package com.we2stars.ticket_desk.service;

import com.we2stars.ticket_desk.dto.UserScoreDTO;
import com.we2stars.ticket_desk.model.AppUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
     Optional<AppUser> getUserById(Long id);

     Optional<AppUser> findByScore(int score);

     List<AppUser> getAllUsers();

     Optional<AppUser> findUserByEmail(String email);

     String register(AppUser user, String password2);

     List<UserScoreDTO> getAllUserScores();

     List<UserScoreDTO> getUserScoresWithinDateRange(LocalDate startDate, LocalDate endDate);
}
