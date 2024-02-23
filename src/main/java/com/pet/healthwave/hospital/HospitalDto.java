package com.pet.healthwave.hospital;

import java.util.List;

public record HospitalDto(
        String headPhysician,
        String hospitalName,
        String address,
        List<String> doctors

) {
}
