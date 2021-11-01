package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository("UpvoteRepository")
public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Upvote SET upvoted = ?1 WHERE nickname_id = ?2 AND question_id = ?3",
            nativeQuery = true
    )
    void upvote(boolean upvoted, long nid, long qid);

    @Query(
            value = "SELECT COUNT(*) FROM Upvote WHERE question_id = ?1 AND upvoted = true",
            nativeQuery = true
    )
    int countUpvotesByQid(long qid);

    @Query(
            value = "DELETE FROM Upvote WHERE question_id = ?1"
    )
    void deleteByQid(long qid);

    @Query(
            value = "SELECT * FROM Upvote WHERE question_id = ?1 AND nickname_id = ?2",
            nativeQuery = true
    )
    Optional<Upvote> findByQidNid(long qid, long nid);

}
