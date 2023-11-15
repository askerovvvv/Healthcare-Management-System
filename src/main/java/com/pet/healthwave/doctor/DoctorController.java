package com.pet.healthwave.doctor;


import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @PatchMapping("/accept")
    public ResponseEntity<?> accept(@RequestParam("id") Long id) {
        doctorService.AcceptDoctor(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/fill/doctor/data")
    public ResponseEntity<?> accept(@RequestBody FillInformationRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.FillInformationForDoctor(request, principal));
    }

    @GetMapping("/profile/{doctorId}")
    public ResponseEntity<DoctorDTO> doctorById(@PathVariable("doctorId") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctorProfileById(id));
    }

}
