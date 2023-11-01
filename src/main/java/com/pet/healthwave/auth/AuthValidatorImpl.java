package com.pet.healthwave.auth;

import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.DefaultValidatorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class AuthValidatorImpl<T> extends DefaultValidatorImpl<T> implements AuthValidator<T> {

    private final UserRepository userRepository;

    @Override
    public List<String> validatePassword(String password, String passwordConfirm) {
        List<String> passwordErrors = new LinkedList<>();

        if (!password.equals(passwordConfirm)) {
            throw new AuthException("Пароли не совпадают!");
        }

        Pattern letterPattern = Pattern.compile("[a-zA-Z]");
        Matcher letterMatcher = letterPattern.matcher(password);
        int letterCount = 0;

        while (letterMatcher.find()) {
            letterCount++;
        }

        if (letterCount < 4) {
            passwordErrors.add(String.format("В пароле %d букв вместо 4", letterCount));
        }

        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);
        int digitCount = 0;

        while (digitMatcher.find()) {
            digitCount++;
        }

        if (digitCount < 1) {
            passwordErrors.add(String.format("В пароле %d цифр вместо 2", digitCount));
        }

        return passwordErrors;
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return userRepository.findByUsername(email).isPresent();
    }


}
