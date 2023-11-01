package com.pet.healthwave.auth;

public class AuthMessages {
    public static final String USER_REGISTERED = "На вашу почту отправлено сообщение, перейдите и активируйте аккаунт.";
    public static final String USER_ACTIVATE_ACCOUNT = "Вы успешно активировали ваш аккаунт! Теперь можете пользоваться вашим аккаунтом.";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "Данный пользователь уже зарегистрирован.";
    public static final String USER_NOT_FOUND_MESSAGE = "Пользователь с данным именем не найден!";
    public static final String BADCREDENTIAL_MESSAGE = "Неверный логин или пароль!";
    public static final String ACCOUNT_ALREADY_VERIFIED_MESSAGE = "Данный аккаунт уже активен!";
    public static final String VERIFY_TOKEN_EXPIRED_MESSAGE = "Ссылка уже неактивна!";
    public static final String TOKEN_NOT_FOUND_MESSAGE = "Данной ссылки для активации не существует!";
    public static final String PASSWORD_REQUIREMENTS_ERROR_MESSAGE = "Пароль должен состоять из минимум 4 букв и 2 цифр.";
}
