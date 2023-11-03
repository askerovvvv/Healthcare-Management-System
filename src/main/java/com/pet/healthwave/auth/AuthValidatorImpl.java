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

/**
 * Extends from default validator to validate fields. This class has own methods to validate -> password and
 * check user from db. It also has generic <T> to validate different objects. For example: request to Register
 * object, request to authenticate objects.
 * @param <T> object to validate
 * @author askerovvvv
 */


@RequiredArgsConstructor
@Component
public class AuthValidatorImpl<T> extends DefaultValidatorImpl<T> implements AuthValidator<T> {

    private final UserRepository userRepository;

    /**
     * Password must match the passwordConfirm. Password must have at list 4 letters and 2 digits.
     * This method examines all these requirements.
     * @param password password from request
     * @param passwordConfirm it should be the same as the password
     * @return description where password is wrong then method that receives this list would add it to logs
     */

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
