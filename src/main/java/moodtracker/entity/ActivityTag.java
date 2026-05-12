package moodtracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "activity_tags")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}