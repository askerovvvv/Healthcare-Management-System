package com.pet.healthwave.auth;

public interface AuthService {
    String registerService(RegisterRequest request);
    AuthenticationResponse authenticateService(AuthenticationRequest request);
}
