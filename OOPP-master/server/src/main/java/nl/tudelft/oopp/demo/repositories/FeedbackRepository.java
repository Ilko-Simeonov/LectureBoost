package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("FeedbackRepository")
@Transactional
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT * FROM Feedback WHERE room_url = ?1", nativeQuery = true)
    List<Feedback> findByUrl(String room);

    @Query (value = "SELECT COUNT(*) FROM Feedback WHERE room_url = ?1 "
            + "AND message = 'Too slow' GROUP BY room_url", nativeQuery = true)
    Integer countTooSlow(String room);

    @Query (value = "SELECT COUNT(*) FROM Feedback WHERE room_url = ?1 "
            + "AND message = 'Too fast' GROUP BY room_url", nativeQuery = true)
    Integer countTooFast(String room);

    @Modifying
    @Query (
            value = "DELETE FROM Feedback WHERE created_time < ?1",
            nativeQuery = true
    )
    void deleteOlderFeedbacks(long tooOldTime);

    @Query(value = "SELECT * FROM Feedback WHERE room_url = ?1 AND nickname_id = ?2",
            nativeQuery = true)
    List<Feedback> findByUrlAndNickNameId(String room, long nickNameId);

}
