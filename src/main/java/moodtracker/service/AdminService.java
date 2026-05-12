package moodtracker.service;

import moodtracker.entity.ActivityTag;
import moodtracker.entity.Emotion;
import moodtracker.entity.User;
import moodtracker.entity.UserRole;
import moodtracker.repository.ActivityTagRepository;
import moodtracker.repository.EmotionRepository;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final ActivityTagRepository activityTagRepository;

    // --- Διαχείριση Θεραπευτών ---
    public List<User> getPendingTherapists() {
        return userRepository.findByRole(UserRole.therapist)
            .stream()
            .filter(u -> !u.isActive())
            .toList();
    }

    @Transactional
    public User approveTherapist(UUID therapistId) {
        User therapist = userRepository.findById(therapistId)
            .orElseThrow(() -> new RuntimeException("Therapist not found"));
        therapist.setActive(true);
        return userRepository.save(therapist);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    // --- Διαχείριση Emotions ---
    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAll();
    }

    @Transactional
    public Emotion createEmotion(String name, String icon) {
        Emotion emotion = Emotion.builder()
            .name(name)
            .icon(icon)
            .build();
        return emotionRepository.save(emotion);
    }

    @Transactional
    public void deleteEmotion(UUID id) {
        emotionRepository.deleteById(id);
    }

    // --- Διαχείριση Activity Tags ---
    public List<ActivityTag> getAllActivityTags() {
        return activityTagRepository.findAll();
    }

    @Transactional
    public ActivityTag createActivityTag(String name) {
        ActivityTag tag = ActivityTag.builder()
            .name(name)
            .build();
        return activityTagRepository.save(tag);
    }

    @Transactional
    public void deleteActivityTag(UUID id) {
        activityTagRepository.deleteById(id);
    }
}