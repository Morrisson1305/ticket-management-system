package com.we2stars.ticket_desk.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String refreshToken;
}
