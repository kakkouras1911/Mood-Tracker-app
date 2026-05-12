package moodtracker.service;

import moodtracker.entity.Reminder;
import moodtracker.entity.User;
import moodtracker.repository.ReminderRepository;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Reminder createReminder(UUID therapistId, UUID patientId,
                                    String message, LocalDateTime scheduledAt) {
        User therapist = userRepository.findById(therapistId)
            .orElseThrow(() -> new RuntimeException("Therapist not found"));
        User patient = userRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        Reminder reminder = Reminder.builder()
            .therapist(therapist)
            .patient(patient)
            .message(message)
            .scheduledAt(scheduledAt)
            .build();

        return reminderRepository.save(reminder);
    }

    public List<Reminder> getPatientReminders(UUID patientId) {
        return reminderRepository.findByPatientIdOrderByScheduledAtDesc(patientId);
    }

    // Τρέχει κάθε λεπτό και στέλνει τις υπενθυμίσεις που έχουν "ωριμάσει"
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processPendingReminders() {
        List<Reminder> pending = reminderRepository
            .findByIsSentFalseAndScheduledAtBefore(LocalDateTime.now());

        for (Reminder reminder : pending) {
            // Εδώ αργότερα μπορούμε να προσθέσουμε email/push notification
            System.out.println("Sending reminder to: " + reminder.getPatient().getEmail());
            reminder.setSent(true);
            reminderRepository.save(reminder);
        }
    }
}