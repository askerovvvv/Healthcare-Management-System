package com.pet.healthwave.hospital;

import jakarta.validation.constraints.NotEmpty;

public record HospitalRequest(
        @NotEmpty(message = "Введите имя главврача!")
        String headPhysician,
        @NotEmpty(message = "Название больницы не может быть пустым!")
        String hospitalName,
        @NotEmpty(message = "Адрес больницы не может быть пустым!")
        String address
) {
}
