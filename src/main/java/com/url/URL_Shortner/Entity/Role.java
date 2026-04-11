package com.url.URL_Shortner.Entity;

import java.util.Set;



import java.util.Set;

public enum Role {
    ADMIN(Set.of(
            Permissions.URL_CREATE,
            Permissions.URL_READ,
            Permissions.URL_ANALYTICS,
            Permissions.ADMIN_CREATE_USER
    )),
    USER(Set.of(
            Permissions.URL_CREATE,
            Permissions.URL_READ,
            Permissions.URL_ANALYTICS
    ));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }
}