package com.we2stars.ticket_desk.service.impl;

import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.dto.RefreshTokenRequest;
import com.we2stars.ticket_desk.execeptions.EmailException;
import com.we2stars.ticket_desk.execeptions.PasswordException;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.model.enums.UserType;
import com.we2stars.ticket_desk.repo.UserRepository;
import com.we2stars.ticket_desk.security.JwtProvider;
import com.we2stars.ticket_desk.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;



    @Override
    public LoginResponse login(AppUser user) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            AppUser dbUser = userRepository.findByEmail(user.getEmail());
            String jwt = jwtProvider.generateToken(dbUser);
            String refreshToken = jwtProvider.generateRefreshToken(dbUser);
            LoginResponse response = new LoginResponse();
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            return response;
    }

    @SneakyThrows
    @Override
    public String registerUser(AppUser user, String secondPassword) {
        if (user.getPassword() != null && !user.getPassword().equals(secondPassword)) {
            throw new PasswordException("Passwords do not match");
        }
        AppUser dbUser = userRepository.findByEmail(user.getEmail());
        if (dbUser != null) {
            throw new EmailException("Email has already been taken");
        }
        user.setType(UserType.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "User successfully registered";
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest tokenRequest) {
        String email = jwtProvider.extractUsername(tokenRequest.getToken());
        AppUser user = userRepository.findByEmail(email);
        if(user==null || !jwtProvider.isTokenValid(tokenRequest.getToken(), user)){
            throw new RuntimeException("Invalid token or User not found");
        }
        String jwt = jwtProvider.generateToken( user);
        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setRefreshToken(tokenRequest.getToken());
        return response;
    }
}
