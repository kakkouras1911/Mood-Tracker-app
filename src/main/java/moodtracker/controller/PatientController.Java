package moodtracker.controller;

import moodtracker.entity.TherapistPatient;
import moodtracker.service.TherapistPatientService;
import moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final TherapistPatientService therapistPatientService;
    private final UserService userService;

    // Δημιουργία token για κοινοποίηση στον θεραπευτή
    @PostMapping("/token/generate")
    public ResponseEntity<Map<String, String>> generateToken(
            @AuthenticationPrincipal String email) {
        UUID patientId = userService.findByEmail(email).getId();
        TherapistPatient connection = therapistPatientService.generateToken(patientId);
        return ResponseEntity.ok(Map.of("accessToken", connection.getAccessToken()));
    }

    // Ανάκληση πρόσβασης
    @DeleteMapping("/token/{connectionId}")
    public ResponseEntity<Void> revokeAccess(@PathVariable UUID connectionId) {
        therapistPatientService.revokeAccess(connectionId);
        return ResponseEntity.noContent().build();
    }
}