package com.pet.healthwave.auth;

public interface AuthService {
    String registerService(RegisterRequest request);
    void activateAccount(String token);
    AuthenticationResponse authenticateService(AuthenticationRequest request);
}
