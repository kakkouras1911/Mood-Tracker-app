package moodtracker.controller;

import moodtracker.entity.ActivityTag;
import moodtracker.entity.Emotion;
import moodtracker.entity.User;
import moodtracker.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

    private final AdminService adminService;

    // Θεραπευτές προς έγκριση
    @GetMapping("/therapists/pending")
    public ResponseEntity<List<User>> getPendingTherapists() {
        return ResponseEntity.ok(adminService.getPendingTherapists());
    }

    @PutMapping("/therapists/{id}/approve")
    public ResponseEntity<User> approveTherapist(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.approveTherapist(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Emotions
    @GetMapping("/emotions")
    public ResponseEntity<List<Emotion>> getEmotions() {
        return ResponseEntity.ok(adminService.getAllEmotions());
    }

    @PostMapping("/emotions")
    public ResponseEntity<Emotion> createEmotion(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            adminService.createEmotion(body.get("name"), body.get("icon"))
        );
    }

    @DeleteMapping("/emotions/{id}")
    public ResponseEntity<Void> deleteEmotion(@PathVariable UUID id) {
        adminService.deleteEmotion(id);
        return ResponseEntity.noContent().build();
    }

    // Activity Tags
    @GetMapping("/activity-tags")
    public ResponseEntity<List<ActivityTag>> getActivityTags() {
        return ResponseEntity.ok(adminService.getAllActivityTags());
    }

    @PostMapping("/activity-tags")
    public ResponseEntity<ActivityTag> createActivityTag(
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(adminService.createActivityTag(body.get("name")));
    }

    @DeleteMapping("/activity-tags/{id}")
    public ResponseEntity<Void> deleteActivityTag(@PathVariable UUID id) {
        adminService.deleteActivityTag(id);
        return ResponseEntity.noContent().build();
    }
} 
    

