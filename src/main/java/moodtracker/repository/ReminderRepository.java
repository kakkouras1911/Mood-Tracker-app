package moodtracker.repository;

import moodtracker.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    // Υπενθυμίσεις που δεν έχουν σταλεί ακόμα
    List<Reminder> findByIsSentFalseAndScheduledAtBefore(LocalDateTime now);

    // Όλες οι υπενθυμίσεις ενός ασθενή
    List<Reminder> findByPatientIdOrderByScheduledAtDesc(UUID patientId);
}