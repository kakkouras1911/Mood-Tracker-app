package moodtracker.controller;


import moodtracker.entity.*;
import moodtracker.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/therapist")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('therapist')")
public class TherapistController {

    private final UserService userService;
    private final MoodLogService moodLogService;
    private final ClinicalNoteService clinicalNoteService;
    private final ReminderService reminderService;
    private final TherapistPatientService therapistPatientService;

    // Λίστα ασθενών με alert indicator
    @GetMapping("/patients")
    public ResponseEntity<List<Map<String, Object>>> getPatients(
            @AuthenticationPrincipal String email) {

        UUID therapistId = userService.findByEmail(email).getId();
        List<TherapistPatient> connections = therapistPatientService.getPatients(therapistId);

        List<Map<String, Object>> result = connections.stream().map(conn -> {
        UUID patientId = conn.getPatient().getId();
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("patientId", patientId);
        map.put("name", conn.getPatient().getName());
        map.put("email", conn.getPatient().getEmail());
        map.put("lowMoodAlert", moodLogService.hasLowMoodAlert(patientId));
        return map;
        }).toList();

        return ResponseEntity.ok(result);
    }

    // Ιστορικό logs ασθενή
    @GetMapping("/patients/{patientId}/logs")
    public ResponseEntity<List<MoodLog>> getPatientLogs(
            @PathVariable UUID patientId) {
        return ResponseEntity.ok(moodLogService.getUserLogs(patientId));
    }

    // Clinical notes
    @GetMapping("/patients/{patientId}/notes")
    public ResponseEntity<List<ClinicalNote>> getNotes(
            @AuthenticationPrincipal String email,
            @PathVariable UUID patientId) {
        UUID therapistId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(clinicalNoteService.getNotes(therapistId, patientId));
    }

    @PostMapping("/patients/{patientId}/notes")
    public ResponseEntity<ClinicalNote> createNote(
            @AuthenticationPrincipal String email,
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body) {
        UUID therapistId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(
            clinicalNoteService.createNote(therapistId, patientId, body.get("content"))
        );
    }

    @PutMapping("/notes/{noteId}")
    public ResponseEntity<ClinicalNote> updateNote(
            @PathVariable UUID noteId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(clinicalNoteService.updateNote(noteId, body.get("content")));
    }

    // Reminders
    @PostMapping("/patients/{patientId}/reminders")
    public ResponseEntity<Reminder> createReminder(
            @AuthenticationPrincipal String email,
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body) {
        UUID therapistId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(reminderService.createReminder(
            therapistId,
            patientId,
            body.get("message"),
            LocalDateTime.parse(body.get("scheduledAt"))
        ));
    }

    // Σύνδεση με token
    @PostMapping("/connect")
    public ResponseEntity<?> connectWithToken(
            @AuthenticationPrincipal String email,
            @RequestBody Map<String, String> body) {
        UUID therapistId = userService.findByEmail(email).getId();
        TherapistPatient connection = therapistPatientService.connect(
            therapistId, body.get("accessToken")
        );
        return ResponseEntity.ok(Map.of("message", "Connected successfully",
                                         "patientId", connection.getPatient().getId()));
    }
 } 
    

