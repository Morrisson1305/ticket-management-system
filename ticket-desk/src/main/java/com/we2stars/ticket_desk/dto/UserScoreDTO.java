package com.we2stars.ticket_desk.dto;

import com.we2stars.ticket_desk.model.enums.UserType;

import lombok.Data;

@Data
public class UserScoreDTO {
    private  String name;
    private  String email;
    private UserType type;
    private int score;

    public UserScoreDTO(String email, String name, int score) {
        this.email = email;
        this.name = name;
        this.score = score;
    }
}
