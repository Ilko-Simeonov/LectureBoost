package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("PollRepository")
public interface PollRepository extends JpaRepository<Poll, Long> {
    /**
     * Find all polls by url.
     *
     * @param url the roomurl
     * @return the polls
     */
    @Query(
            value = "SELECT * FROM Poll WHERE room_url = ?1",
            nativeQuery = true
    )
    List<Poll> findByUrl(String url);

    /**
     * Find all polls by url. (active polls first)
     *
     * @param url the roomurl
     * @return the polls
     */
    @Query(
            value = "SELECT * FROM Poll WHERE room_url = ?1 ORDER BY active DESC",
            nativeQuery = true
    )
    List<Poll> findByUrlOrdered(String url);

    /**
     * Find all active polls by url.
     *
     * @param url the roomurl
     * @return the (active) polls
     */
    @Query(
            value = "SELECT * FROM Poll WHERE room_url = ?1 AND active = true",
            nativeQuery = true
    )
    List<Poll> findActiveByUrl(String url);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Poll SET active = ?2 WHERE id = ?1",
            nativeQuery = true
    )
    void setActive(long pid, boolean active);
}
