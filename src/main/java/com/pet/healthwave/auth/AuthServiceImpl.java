package com.pet.healthwave.auth;

import com.pet.healthwave.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;


    @Override
    public String registerService(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse authenticateService(AuthenticationRequest request) {
        return null;
    }
}
