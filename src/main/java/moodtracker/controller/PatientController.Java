package moodtracker.controller;

import moodtracker.entity.TherapistPatient;
import moodtracker.service.TherapistPatientService;
import moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final TherapistPatientService therapistPatientService;
    private final UserService userService;

    // Δημιουργία token για κοινοποίηση στον θεραπευτή
   @PostMapping("/token/generate")
public ResponseEntity<Map<String, String>> generateToken(
        @AuthenticationPrincipal String email) {
    UUID patientId = userService.findByEmail(email).getId();
    
    // Έλεγξε αν υπάρχει ήδη active token χωρίς θεραπευτή
    List<TherapistPatient> existing = therapistPatientService
        .getPatientConnections(patientId);
    
    // Αν υπάρχει ήδη token χωρίς θεραπευτή, επέστρεψέ το
    for (TherapistPatient tp : existing) {
        if (tp.getTherapist() == null) {
            return ResponseEntity.ok(Map.of("accessToken", tp.getAccessToken()));
        }
    }
    
    TherapistPatient connection = therapistPatientService.generateToken(patientId);
    return ResponseEntity.ok(Map.of("accessToken", connection.getAccessToken()));
}

    // Ανάκληση πρόσβασης
    @DeleteMapping("/token/{connectionId}")
    public ResponseEntity<Void> revokeAccess(@PathVariable UUID connectionId) {
        therapistPatientService.revokeAccess(connectionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/connections")
public ResponseEntity<List<TherapistPatient>> getConnections(
        @AuthenticationPrincipal String email) {
    UUID patientId = userService.findByEmail(email).getId();
    return ResponseEntity.ok(therapistPatientService.getPatientConnections(patientId));
}
}