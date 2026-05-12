package moodtracker.repository;

import moodtracker.entity.TherapistPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TherapistPatientRepository extends JpaRepository<TherapistPatient, UUID> {

    // Όλοι οι ασθενείς ενός θεραπευτή
    List<TherapistPatient> findByTherapistIdAndIsActiveTrue(UUID therapistId);

    // Όλοι οι θεραπευτές ενός ασθενή
    List<TherapistPatient> findByPatientIdAndIsActiveTrue(UUID patientId);

    // Εύρεση σύνδεσης με token (για κοινοποίηση δεδομένων)
    Optional<TherapistPatient> findByAccessTokenAndIsActiveTrue(String accessToken);

    // Έλεγχος αν υπάρχει ήδη σύνδεση
    boolean existsByTherapistIdAndPatientId(UUID therapistId, UUID patientId);
}