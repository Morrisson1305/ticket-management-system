package com.we2stars.ticket_desk.controller;

import com.we2stars.ticket_desk.dto.RegistrationRequest;
import com.we2stars.ticket_desk.dto.UserScoreDTO;
import com.we2stars.ticket_desk.exceptions.InputFieldException;
import com.we2stars.ticket_desk.modal_mapper.AuthenticationMapper;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;


    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public ResponseEntity<List<AppUser>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public ResponseEntity<Optional<AppUser>> getUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/admin/users/{score}")
    @PreAuthorize("hasRole('ROLE_TICKET_MANAGER')")
    public  ResponseEntity<Optional<AppUser>> getUserByScore(@PathVariable("score") int score){
        return ResponseEntity.ok(userService.findByScore(score));
    }

    @GetMapping("/admin/users/{email}")
    @PreAuthorize("hasRole('TICKET_MANAGER')")
    public ResponseEntity<AppUser> getUserByEmail(@RequestBody String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }



    @GetMapping("/admin/score")
    @PreAuthorize("hasRole('TICKET_MANAGER')")
    public ResponseEntity<List<UserScoreDTO>> getUserScores() {
        List<UserScoreDTO> userScores = userService.getAllUserScores();
        return ResponseEntity.ok(userScores);
    }

    @GetMapping("/admin/scores/range")
    @PreAuthorize("hasRole('TICKET_MANAGER')")
    public ResponseEntity<List<UserScoreDTO>> getUserScoresWithinDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<UserScoreDTO> userScores = userService.getUserScoresWithinDateRange(startDate, endDate);
        return ResponseEntity.ok(userScores);
    }


}
