package com.pet.healthwave.user;

import java.security.Principal;
import java.util.List;

public interface UserService {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    List<UserDTO> getAllUsers();
}
