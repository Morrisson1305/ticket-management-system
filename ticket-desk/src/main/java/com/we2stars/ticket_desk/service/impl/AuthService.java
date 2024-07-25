package com.we2stars.ticket_desk.service.impl;

import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;

    public LoginResponse login(AppUser userPrinciple) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userPrinciple.getEmail(), userPrinciple.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse response = new LoginResponse();
        response.setEmail(userPrinciple.getEmail());
        response.setMessage("Login successful");
        return response;

    }


}
