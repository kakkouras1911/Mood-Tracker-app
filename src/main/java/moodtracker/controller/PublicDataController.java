package moodtracker.controller;

import moodtracker.entity.ActivityTag;
import moodtracker.entity.Emotion;
import moodtracker.repository.ActivityTagRepository;
import moodtracker.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicDataController {

    private final EmotionRepository emotionRepository;
    private final ActivityTagRepository activityTagRepository;

    @GetMapping("/emotions")
    public ResponseEntity<List<Emotion>> getEmotions() {
        return ResponseEntity.ok(emotionRepository.findByIsActiveTrue());
    }

    @GetMapping("/activity-tags")
    public ResponseEntity<List<ActivityTag>> getActivityTags() {
        return ResponseEntity.ok(activityTagRepository.findByIsActiveTrue());
    }
}