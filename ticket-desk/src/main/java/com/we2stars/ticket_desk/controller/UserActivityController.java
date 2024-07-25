package com.we2stars.ticket_desk.controller;

import com.we2stars.ticket_desk.model.UserActivity;
import com.we2stars.ticket_desk.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class UserActivityController {

    private SimpMessagingTemplate messagingTemplate;

    private UserActivityService service;

    @MessageMapping("/activities")
    @SendTo("/topic/activities")
    public List<UserActivity> getActivities() {
        return service.getAllActivities();
    }

    public void sendActivity(UserActivity activity) {
        messagingTemplate.convertAndSend("/topic/activities", activity);
    }
}
