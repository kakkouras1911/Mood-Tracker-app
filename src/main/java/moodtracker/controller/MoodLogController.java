package moodtracker.controller;

import moodtracker.entity.MoodLog;
import moodtracker.service.MoodLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class MoodLogController {

    private final MoodLogService moodLogService;
    private final moodtracker.service.UserService userService;

    @PostMapping
    public ResponseEntity<MoodLog> createLog(
            @AuthenticationPrincipal String email,
            @RequestBody Map<String, Object> body) {

        UUID userId = userService.findByEmail(email).getId();

        MoodLog log = moodLogService.createLog(
            userId,
            (Integer) body.get("moodScore"),
            new BigDecimal(body.get("sleepHours").toString()),
            (Boolean) body.get("exercised"),
            (String) body.get("notes"),
            toUUIDSet((List<?>) body.get("emotionIds")),
            toUUIDSet((List<?>) body.get("activityIds"))
        );
        return ResponseEntity.ok(log);
    }

            @PutMapping("/{logId}")
        public ResponseEntity<MoodLog> updateLog(
                @AuthenticationPrincipal String email,
                @PathVariable UUID logId,
                @RequestBody Map<String, Object> body) {

            MoodLog log = moodLogService.updateLog(
                logId,
                (Integer) body.get("moodScore"),
                new BigDecimal(body.get("sleepHours").toString()),
                (Boolean) body.get("exercised"),
                (String) body.get("notes"),
                toUUIDSet((List<?>) body.get("emotionIds")),
                toUUIDSet((List<?>) body.get("activityIds"))
            );
            return ResponseEntity.ok(log);
        }

    @GetMapping
    public ResponseEntity<List<MoodLog>> getLogs(
            @AuthenticationPrincipal String email) {
        UUID userId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(moodLogService.getUserLogs(userId));
    }

    @GetMapping("/range")
    public ResponseEntity<List<MoodLog>> getLogsInRange(
            @AuthenticationPrincipal String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        UUID userId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(moodLogService.getUserLogsInRange(userId, from, to));
    }

    @GetMapping("/stats/average")
    public ResponseEntity<Map<String, Double>> getAverage(
            @AuthenticationPrincipal String email) {
        UUID userId = userService.findByEmail(email).getId();
        return ResponseEntity.ok(Map.of("average", moodLogService.getAverageMood(userId)));
    }

    private Set<UUID> toUUIDSet(List<?> list) {
        if (list == null) return Set.of();
        return Set.copyOf(list.stream()
            .map(id -> UUID.fromString(id.toString()))
            .toList());
    }
}