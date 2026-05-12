package moodtracker.repository;

import moodtracker.entity.ClinicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, UUID> {

    // Σημειώσεις θεραπευτή για συγκεκριμένο ασθενή
    List<ClinicalNote> findByTherapistIdAndPatientIdOrderByCreatedAtDesc(
        UUID therapistId, UUID patientId
    );
}