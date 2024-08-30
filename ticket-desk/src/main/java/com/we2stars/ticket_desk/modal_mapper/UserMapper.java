package com.we2stars.ticket_desk.modal_mapper;

import com.we2stars.ticket_desk.dto.RegistrationRequest;
import com.we2stars.ticket_desk.model.AppUser;
import com.we2stars.ticket_desk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public AppUser convertToEntity(RegistrationRequest registrationRequest){
        return modelMapper.map(registrationRequest, AppUser.class);
    }
}
