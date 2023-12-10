package com.pet.healthwave.doctor;


import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.logging.Logger;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @PatchMapping("/accept")
    public ResponseEntity<?> accept(@RequestParam("id") Long id, Principal principal) {
        doctorService.AcceptDoctor(id, principal);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/fill/doctor/data")
    public ResponseEntity<?> fillDoctorData(@RequestBody FillInformationRequest request, Principal principal) {
        doctorService.FillInformationForDoctor(request, principal);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change-headPhysician")
    // TODO: FIX PreAuthorize
    public ResponseEntity<?> changeHeadPhysician(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("hospitalId") Integer hospitalId
            )
    {
        doctorService.changeHeadPhysician(doctorId, hospitalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile/{doctorId}")
    public ResponseEntity<DoctorDTO> doctorById(@PathVariable("doctorId") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctorProfileById(id));
    }

}
