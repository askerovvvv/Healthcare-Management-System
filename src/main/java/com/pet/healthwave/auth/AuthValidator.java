package com.pet.healthwave.auth;

import com.pet.healthwave.validator.DefaultValidator;

import java.util.List;

public interface AuthValidator<T> extends DefaultValidator<T>{

    List<String> validatePassword(String password, String passwordConfirm);

    boolean checkIfUserExists(String email);
}
