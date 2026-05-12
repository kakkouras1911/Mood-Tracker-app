package moodtracker.service;

import moodtracker.entity.ClinicalNote;
import moodtracker.entity.User;
import moodtracker.repository.ClinicalNoteRepository;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicalNoteService {

    private final ClinicalNoteRepository clinicalNoteRepository;
    private final UserRepository userRepository;

    @Transactional
    public ClinicalNote createNote(UUID therapistId, UUID patientId, String content) {
        User therapist = userRepository.findById(therapistId)
            .orElseThrow(() -> new RuntimeException("Therapist not found"));
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        ClinicalNote note = ClinicalNote.builder()
            .therapist(therapist)
            .patient(patient)
            .content(content)
            .build();

        return clinicalNoteRepository.save(note);
    }

    @Transactional
    public ClinicalNote updateNote(UUID noteId, String content) {
        ClinicalNote note = clinicalNoteRepository.findById(noteId)
            .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return clinicalNoteRepository.save(note);
    }

    public List<ClinicalNote> getNotes(UUID therapistId, UUID patientId) {
        return clinicalNoteRepository
            .findByTherapistIdAndPatientIdOrderByCreatedAtDesc(therapistId, patientId);
    }
}