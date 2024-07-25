package com.we2stars.ticket_desk.service.impl;

import com.we2stars.ticket_desk.controller.UserActivityController;
import com.we2stars.ticket_desk.model.UserActivity;
import com.we2stars.ticket_desk.repo.UserActivityRepository;
import com.we2stars.ticket_desk.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityImpl implements UserActivityService {

    private final UserActivityRepository activityRepository;
    private final UserActivityController activityController;
    @Override
    public void logActivity(String username, String action) {
        UserActivity activity = new UserActivity();
        activity.setUsername(username);
        activity.setAction(action);
        activity.setTimestamp(LocalDateTime.now());
        activityRepository.save(activity);

        activityController.sendActivity(activity);

    }

    @Override
    public List<UserActivity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public List<UserActivity> getActivitiesByUsername(String username) {
        return activityRepository.findByUsername(username);
    }
}
