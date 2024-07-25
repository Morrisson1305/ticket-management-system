package com.we2stars.ticket_desk.execeptions;

import lombok.Getter;

@Getter
public class PasswordException extends RuntimeException {
    private final String passwordError;

    public PasswordException(String passwordError) {
        this.passwordError = passwordError;
    }
}

