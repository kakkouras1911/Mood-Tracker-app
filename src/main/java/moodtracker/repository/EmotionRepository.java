package moodtracker.repository;

import moodtracker.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, UUID> {

    List<Emotion> findByIsActiveTrue();
}