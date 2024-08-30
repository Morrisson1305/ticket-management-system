package com.we2stars.ticket_desk.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserPermission {

    TICKET_MANAGER_READ("admin:read"),
    TICKET_MANAGER_UPDATE("admin:update"),
    TICKET_MANAGER_CREATE("admin:create"),
    TICKET_MANAGER_DELETE("admin:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete");

    private final String userPermission;
}
