package com.we2stars.ticket_desk.service;

import com.we2stars.ticket_desk.model.UserActivity;

import java.util.List;

public interface UserActivityService {

    void logActivity(String username, String action);
    List<UserActivity> getAllActivities();

    List<UserActivity> getActivitiesByUsername(String username);
}

