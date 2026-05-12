package moodtracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "mood_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "log_date"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MoodLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "mood_score", nullable = false)
    private int moodScore; // 1-5

    @Column(name = "sleep_hours", precision = 4, scale = 1)
    private BigDecimal sleepHours;

    @Column(nullable = false)
    @Builder.Default
    private boolean exercised = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "mood_log_emotions",
        joinColumns = @JoinColumn(name = "mood_log_id"),
        inverseJoinColumns = @JoinColumn(name = "emotion_id")
    )
    @Builder.Default
    private Set<Emotion> emotions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "mood_log_activities",
        joinColumns = @JoinColumn(name = "mood_log_id"),
        inverseJoinColumns = @JoinColumn(name = "activity_tag_id")
    )
    @Builder.Default
    private Set<ActivityTag> activities = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.logDate == null) this.logDate = LocalDate.now();
    }
}