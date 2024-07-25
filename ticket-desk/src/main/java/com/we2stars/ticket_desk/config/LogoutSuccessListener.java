package com.we2stars.ticket_desk.config;

import com.we2stars.ticket_desk.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

    private final UserActivityService userActivityService;
    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            userActivityService.logActivity(username, "Logged out");
        }
    }
}