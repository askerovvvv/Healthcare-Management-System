package com.pet.healthwave.hospital;


import com.pet.healthwave.doctor.Specialty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all-hospitals")
    public ResponseEntity<?> getAllHospitals(
            @RequestParam(required = false) Specialty specialty
    ) {
        return ResponseEntity.ok(hospitalService.getAllHospitals(specialty));
    }
}
