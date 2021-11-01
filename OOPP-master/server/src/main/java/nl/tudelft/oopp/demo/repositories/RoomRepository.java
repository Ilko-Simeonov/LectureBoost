package nl.tudelft.oopp.demo.repositories;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository("RoomRepository")
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Modifying
    @Transactional

    @Query(value = "UPDATE room SET open = ?1 WHERE url = ?2 AND starttime < ?3"
            + " AND endtime > ?3 AND date = ?4", nativeQuery = true)
    void openScheduled(boolean open, String url, Time time, Date date);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room SET open = ?1 WHERE url = ?2", nativeQuery = true)
    void updateOpenByUrl(boolean open, String url);

    @Query(value = "SELECT * FROM Room WHERE url = ?1 AND starttime < ?2 "
            + "AND endtime > ?2 AND date = ?3", nativeQuery = true)
    Optional<Room> scheduledRoom(String url, Time time, Date date);

    @Query(value = "SELECT * FROM Room WHERE url = ?1", nativeQuery = true)
    Optional<Room> findByUrl(String url);

    @Query(value = "SELECT * FROM Room WHERE url = ?1", nativeQuery = true)
    List<Room> findAllByUrl(String url);

    @Query(value = "SELECT * FROM Room ", nativeQuery = true)
    List<Room> findAll();

    @Query(value = "DELETE * FROM Room WHERE url = ?1", nativeQuery = true)
    void deleteByUrl(String url);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Room "
                    + "SET name = ?2, moderator_password = ?3, open = ?4, "
                    + "starttime = ?5, endtime = ?6 "
                    + "WHERE url = ?1",
            nativeQuery = true)
    void editRoom(String url, String name, String modPassword, boolean open,
                  long startTime, long endTime);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE Room SET question_cooldown = ?2 WHERE url = ?1",
            nativeQuery = true)
    void setCooldown(String url, int cd);
}
