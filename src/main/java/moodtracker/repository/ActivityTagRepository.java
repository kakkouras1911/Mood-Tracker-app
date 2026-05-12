package moodtracker.repository;

import moodtracker.entity.ActivityTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityTagRepository extends JpaRepository<ActivityTag, UUID> {

    List<ActivityTag> findByIsActiveTrue();
}