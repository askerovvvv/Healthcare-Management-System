package com.pet.healthwave.handler;

import java.time.LocalDateTime;

/**
 * When catch some exception server will fill in these fields and return to client
 * Don't need to use class, record is better.
 * @param path url where exception happened
 * @param message description of exception
 * @param status http status
 * @param dateTime time when it happened
 * @author askerovvvv
 */

public record ApiError(
        String path,
        String message,
        int status,
        LocalDateTime dateTime
) {
}
