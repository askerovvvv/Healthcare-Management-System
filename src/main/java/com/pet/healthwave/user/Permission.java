package com.pet.healthwave.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    DOCTOR_READ("DOCTOR:read"),
    DOCTOR_UPDATE("DOCTOR:update"),
    DOCTOR_CREATE("DOCTOR:create"),
    DOCTOR_DELETE("DOCTOR:delete"),

    HEAD_PHYSICIAN_READ("HEAD_PHYSICIAN:read"),
    HEAD_PHYSICIAN_UPDATE("HEAD_PHYSICIAN:update"),
    HEAD_PHYSICIAN_CREATE("HEAD_PHYSICIAN:create"),
    HEAD_PHYSICIAN_DELETE("HEAD_PHYSICIAN:delete");

    @Getter
    private final String permission;

}
