package com.pet.healthwave.handler;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String message,
        int status,
        LocalDateTime dateTime
) {
}
