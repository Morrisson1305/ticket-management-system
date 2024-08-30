package com.we2stars.ticket_desk.dto;


import com.we2stars.ticket_desk.model.enums.UserType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank(message = "user name required")
    private String name;
    @NotBlank(message = "user email required")
    @Email(message = "Incorrect email address format")
    private String email;
    @Size(min = 8, max = 16, message = "The password must be between 6 and 16 characters long")
    private String password;
    @Size(min = 8, max = 16, message = "The password must be between 6 and 16 characters long")
    private String secondPassword;
    @Enumerated
    private UserType role;
}
