package com.pet.healthwave.auth;

/**
 * If user authenticates server would return JWT token. Don't need to user class, record is better
 *
 * @param token returning JWT token
 *
 * @author askerovvvv
 */
public record AuthenticationResponse(
        String token
) {

}
