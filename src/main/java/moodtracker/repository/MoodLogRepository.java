package moodtracker.repository;

import moodtracker.entity.MoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, UUID> {

    // Όλα τα logs ενός χρήστη, ταξινομημένα από νεότερο
    List<MoodLog> findByUserIdOrderByLogDateDesc(UUID userId);

    // Log συγκεκριμένης ημέρας
    Optional<MoodLog> findByUserIdAndLogDate(UUID userId, LocalDate logDate);

    // Logs σε εύρος ημερομηνιών (για γραφήματα)
    List<MoodLog> findByUserIdAndLogDateBetweenOrderByLogDateAsc(
        UUID userId, LocalDate from, LocalDate to
    );

    // Μέσος όρος διάθεσης ανά χρήστη (για στατιστικά θεραπευτή)
    @Query("SELECT AVG(m.moodScore) FROM MoodLog m WHERE m.user.id = :userId")
    Double findAverageMoodByUserId(@Param("userId") UUID userId);

    // Συνεχόμενες μέρες αρνητικής διάθεσης (alert για θεραπευτή)
    @Query("""
        SELECT m FROM MoodLog m
        WHERE m.user.id = :userId
        AND m.moodScore <= 2
        AND m.logDate >= :since
        ORDER BY m.logDate DESC
    """)
    List<MoodLog> findLowMoodLogs(@Param("userId") UUID userId,
                                   @Param("since") LocalDate since);
}