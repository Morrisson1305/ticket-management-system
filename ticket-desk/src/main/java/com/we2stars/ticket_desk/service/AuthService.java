package com.we2stars.ticket_desk.service;

import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.dto.RefreshTokenRequest;
import com.we2stars.ticket_desk.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


public interface AuthService {

    LoginResponse login(AppUser userPrinciple);
    String registerUser(AppUser user, String secondPassword);

    LoginResponse refreshToken(RefreshTokenRequest tokenRequest);

}
