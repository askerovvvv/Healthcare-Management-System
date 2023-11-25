package com.pet.healthwave.hospital;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping("/add-hospital")
    public ResponseEntity<?> addHospital(@RequestBody HospitalRequest request) {
        hospitalService.addHospital(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
