package com.pet.healthwave.doctor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record FillInformationRequest (
        @NotNull(message = "Стаж работы не может быть пустым!")
        Byte experience,
        @NotEmpty(message = "Ваша специальность не может быть пустым!")
        Specialty specialty,
        @NotEmpty(message = "Ваши квалификации не могут быть пустыми!")
        Set<String> qualifications
) {
}
