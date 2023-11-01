package com.pet.healthwave.auth;

public interface AuthService {
    String registerService(RegisterRequest request);
    String confirmAccount(String token);
    AuthenticationResponse authenticateService(AuthenticationRequest request);
}
