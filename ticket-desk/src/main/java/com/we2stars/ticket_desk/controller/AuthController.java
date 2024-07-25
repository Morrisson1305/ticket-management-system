package com.we2stars.ticket_desk.controller;

import com.we2stars.ticket_desk.dto.LoginRequest;
import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AppUser userPrinciple = new AppUser();
        userPrinciple.setEmail(request.getEmail());
        userPrinciple.setPassword(request.getPassword());
        LoginResponse response = authService.login(userPrinciple);
        return ResponseEntity.ok(response);
    }

}
