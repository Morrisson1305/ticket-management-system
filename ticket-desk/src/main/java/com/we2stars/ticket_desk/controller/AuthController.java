package com.we2stars.ticket_desk.controller;

import com.we2stars.ticket_desk.dto.LoginRequest;
import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.dto.RegistrationRequest;
import com.we2stars.ticket_desk.exceptions.InputFieldException;
import com.we2stars.ticket_desk.modal_mapper.AuthenticationMapper;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AppUser userPrinciple = new AppUser();
        userPrinciple.setEmail(request.getEmail());
        userPrinciple.setPassword(request.getPassword());
        LoginResponse response = authService.login(userPrinciple);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationMapper.registerUser(request));
    }

}
