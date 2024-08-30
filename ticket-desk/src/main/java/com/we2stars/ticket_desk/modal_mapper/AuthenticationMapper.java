package com.we2stars.ticket_desk.modal_mapper;

import com.we2stars.ticket_desk.dto.LoginResponse;
import com.we2stars.ticket_desk.dto.RefreshTokenRequest;
import com.we2stars.ticket_desk.dto.RegistrationRequest;
import com.we2stars.ticket_desk.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {
    private final AuthService authService;
    private final UserMapper userMapper;

    public String registerUser(RegistrationRequest registrationRequest){
        return authService.registerUser(userMapper.convertToEntity(registrationRequest), registrationRequest.getSecondPassword());
    }

    public LoginResponse refreshToken(RefreshTokenRequest tokenRequest){
        return authService.refreshToken(tokenRequest);
    }
}
