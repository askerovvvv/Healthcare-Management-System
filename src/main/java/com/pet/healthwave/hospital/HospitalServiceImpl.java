package com.pet.healthwave.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService{

    private final HospitalRepository hospitalRepository;

    @Override
    public void addHospital(HospitalRequest request) {
        Hospital hospital = Hospital.builder()
                .hospitalName(request.hospitalName())
                .address(request.address())
                .headPhysician(request.headPhysician())
                .build();

        hospitalRepository.save(hospital);
    }
}
