package moodtracker.service;

import moodtracker.entity.TherapistPatient;
import moodtracker.entity.User;
import moodtracker.repository.TherapistPatientRepository;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TherapistPatientService {

    private final TherapistPatientRepository therapistPatientRepository;
    private final UserRepository userRepository;

    @Transactional
    public TherapistPatient connect(UUID therapistId, String accessToken) {
        TherapistPatient connection = therapistPatientRepository
            .findByAccessTokenAndIsActiveTrue(accessToken)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // Ο θεραπευτής συνδέεται με τον ασθενή μέσω token
        User therapist = userRepository.findById(therapistId)
            .orElseThrow(() -> new RuntimeException("Therapist not found"));

        connection.setTherapist(therapist);
        connection.setActive(true);
        return therapistPatientRepository.save(connection);
    }

    @Transactional
public TherapistPatient generateToken(UUID patientId) {
    User patient = userRepository.findById(patientId)
        .orElseThrow(() -> new RuntimeException("Patient not found"));

    // Έλεγξε αν υπάρχει ήδη active token χωρίς therapist
    TherapistPatient connection = TherapistPatient.builder()
        .patient(patient)
        .accessToken(java.util.UUID.randomUUID().toString())
        .isActive(true)
        .build();

    return therapistPatientRepository.save(connection);
}

    @Transactional
    public void revokeAccess(UUID connectionId) {
        TherapistPatient connection = therapistPatientRepository.findById(connectionId)
            .orElseThrow(() -> new RuntimeException("Connection not found"));

        connection.setActive(false);
        connection.setRevokedAt(LocalDateTime.now());
        therapistPatientRepository.save(connection);
    }

    public List<TherapistPatient> getPatients(UUID therapistId) {
        return therapistPatientRepository.findByTherapistIdAndIsActiveTrue(therapistId);
    }

    public List<TherapistPatient> getPatientConnections(UUID patientId) {
    return therapistPatientRepository.findByPatientIdAndIsActiveTrue(patientId);
}
}   