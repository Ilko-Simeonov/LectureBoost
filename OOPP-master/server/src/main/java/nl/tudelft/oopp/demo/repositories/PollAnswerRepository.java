package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("PollAnswerRepository")
public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {
    @Query(
            value = "SELECT * FROM PollAnswer WHERE poll_id = ?1 AND nickname_id = ?2",
            nativeQuery = true
    )
    Optional<PollAnswer> findByPollIdAndNicknameId(long pollId, long nicknameId);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE PollAnswer SET answer = ?2 WHERE id = ?1",
            nativeQuery = true
    )
    void updateById(long id, int answer);

    @Query(
            value = "SELECT COUNT(*) FROM PollAnswer WHERE poll_id = ?1 AND answer = ?2",
            nativeQuery = true
    )
    int getAmountOfAnswers(long pollId, int option);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM PollAnswer WHERE poll_id = ?1",
            nativeQuery = true
    )
    void deleteByPid(long pid);
}
