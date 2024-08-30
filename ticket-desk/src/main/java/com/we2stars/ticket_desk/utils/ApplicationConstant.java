package com.we2stars.ticket_desk.utils;

public class ApplicationConstant {
    public static final String[] USER_URLS = {"/user/{userId}/ticket/{ticketId}/status","/user/{userId}/status","/api/v1/users/**"};
    public static final String[] ADMIN_URLS = {"/api/v1/admin/**"};

    public static final String[] PUBLIC_URLS = {"/api/v1/login", "/api/v1/register"};
}
