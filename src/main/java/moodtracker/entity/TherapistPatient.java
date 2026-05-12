package moodtracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "therapist_patient", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"therapist_id", "patient_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TherapistPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    private User therapist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private LocalDateTime grantedAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @PrePersist
    protected void onCreate() {
        this.grantedAt = LocalDateTime.now();
        if (this.accessToken == null)
            this.accessToken = UUID.randomUUID().toString();
    }
}