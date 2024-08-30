package com.we2stars.ticket_desk.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.we2stars.ticket_desk.model.enums.UserPermission.*;

@Getter
@RequiredArgsConstructor
public enum UserType {
    TICKET_MANAGER(
            Set.of(
                    TICKET_MANAGER_READ,
                    TICKET_MANAGER_UPDATE,
                    TICKET_MANAGER_CREATE,
                    TICKET_MANAGER_DELETE
            )
    ),
    USER(
            Set.of(
                    USER_READ,
                    USER_UPDATE,
                    USER_CREATE,
                    USER_DELETE
            )
    );

    private final Set<UserPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getUserPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
