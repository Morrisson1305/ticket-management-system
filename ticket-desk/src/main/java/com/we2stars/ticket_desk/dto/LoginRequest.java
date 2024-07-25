package com.we2stars.ticket_desk.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
