package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("NicknameRepository")
public interface NicknameRepository extends JpaRepository<Nickname, Long> {

    @Query(value = "SELECT * FROM Nickname WHERE room_url = ?1", nativeQuery = true)
    List<Nickname> findByUrl(String url);

    @Query(value = "SELECT * FROM Nickname WHERE room_url = ?1 AND moderator = true",
            nativeQuery = true)
    List<Nickname> findModeratorsByUrl(String url);

    @Query(value = "SELECT * FROM Nickname WHERE room_url = ?1 AND name = ?2",
            nativeQuery = true)
    Optional<Nickname> findByUrlAndName(String url, String name);

    @Query(
            value = "SELECT COUNT(*) FROM Nickname WHERE room_url = ?1 AND active = true",
            nativeQuery = true
    )
    int amountOfParticipants(String roomUrl);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Nickname SET active = ?2 WHERE id = ?1"
    )
    void leaveById(long id, boolean active);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Nickname SET name = ?2 WHERE id = ?1"
    )
    void change(long nid, String newNickname);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Nickname SET muted = true WHERE id = ?1"
    )
    void muteById(long nid);
}
