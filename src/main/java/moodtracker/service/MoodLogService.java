package moodtracker.service;

import moodtracker.entity.*;
import moodtracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoodLogService {

    private final MoodLogRepository moodLogRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final ActivityTagRepository activityTagRepository;

    @Transactional
    public MoodLog createLog(UUID userId, int moodScore, BigDecimal sleepHours,
                              boolean exercised, String notes,
                              Set<UUID> emotionIds, Set<UUID> activityIds) {

        if (moodLogRepository.findByUserIdAndLogDate(userId, LocalDate.now()).isPresent())
            throw new RuntimeException("Log already exists for today");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Emotion> emotions = Set.copyOf(emotionRepository.findAllById(emotionIds));
        Set<ActivityTag> activities = Set.copyOf(activityTagRepository.findAllById(activityIds));

        MoodLog log = MoodLog.builder()
            .user(user)
            .moodScore(moodScore)
            .sleepHours(sleepHours)
            .exercised(exercised)
            .notes(notes)
            .logDate(LocalDate.now())
            .emotions(emotions)
            .activities(activities)
            .build();

        return moodLogRepository.save(log);
    }

    @Transactional
public MoodLog updateLog(UUID logId, int moodScore, BigDecimal sleepHours,
                          boolean exercised, String notes,
                          Set<UUID> emotionIds, Set<UUID> activityIds) {

    MoodLog log = moodLogRepository.findById(logId)
        .orElseThrow(() -> new RuntimeException("Log not found"));

    log.setMoodScore(moodScore);
    log.setSleepHours(sleepHours);
    log.setExercised(exercised);
    log.setNotes(notes);
    
    // Χρήση mutable HashSet αντί για immutable Set.copyOf()
    log.setEmotions(new java.util.HashSet<>(emotionRepository.findAllById(emotionIds)));
    log.setActivities(new java.util.HashSet<>(activityTagRepository.findAllById(activityIds)));

    return moodLogRepository.save(log);
}

    public List<MoodLog> getUserLogs(UUID userId) {
        return moodLogRepository.findByUserIdOrderByLogDateDesc(userId);
    }

    public List<MoodLog> getUserLogsInRange(UUID userId, LocalDate from, LocalDate to) {
        return moodLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, from, to);
    }

    public Double getAverageMood(UUID userId) {
        return moodLogRepository.findAverageMoodByUserId(userId);
    }

    // Ελέγχει αν ο ασθενής έχει συνεχόμενες μέρες αρνητικής διάθεσης
    public boolean hasLowMoodAlert(UUID userId) {
        LocalDate since = LocalDate.now().minusDays(3);
        List<MoodLog> lowLogs = moodLogRepository.findLowMoodLogs(userId, since);
        return lowLogs.size() >= 3;
    }
}