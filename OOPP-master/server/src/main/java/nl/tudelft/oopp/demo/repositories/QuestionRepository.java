package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository("QuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {
    //All of the queries below that get questions ONLY get the unanswered questions,
    //unless otherwise specified.

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE question SET upvotes = upvotes + 1 WHERE id = ?1",
            nativeQuery = true
    )
    void upvoteById(long id);

    /*
     The purpose of the following method is to sort ALL questions by number of upvotes
     */
    List<Question> findAllByOrderByUpvotesDesc();

    /**
     * Find unanswered questions by url.
     *
     * @param url the url
     * @return the questions
     */
    @Query(
            value = "SELECT * FROM Question WHERE room_url = ?1  AND answered = false",
            nativeQuery = true
    )
    List<Question> findByUrl(String url);

    /**
     * Find unanswered questions by url, ordered by upvotes (descending).
     *
     * @param url the url
     * @return the questions
     */
    @Query(
            value = "SELECT * "
                    + "FROM Question "
                    + "WHERE room_url = ?1 AND answered = false "
                    + "ORDER BY upvotes DESC",
            nativeQuery = true
    )
    List<Question> findByUrlOrdered(String url);

    @Query(value = "DELETE question FROM Question WHERE id=?1", nativeQuery = true)
    void deleteById(long id);

    /*
     The purpose of the following method is to list all questions asked by a specified user
     */
    @Query(value = "SELECT * FROM Question WHERE room_url = ?1 AND nickname_id = ?2",
            nativeQuery = true)
    List<Question> findByUrlAndUserID(String url, int nicknameid);


    /**
     * Find all answered questions.
     *
     * @return the questions
     */
    @Query(
            value = "SELECT * FROM Question WHERE answered = true",
            nativeQuery = true
    )
    List<Question> findAnswered();

    /**
     * Find all answered questions for a certain url.
     *
     * @param url the url
     * @return the questions
     */
    @Query(
            value = "SELECT * "
                    + "FROM Question "
                    + "WHERE room_url = ?1 AND answered = true",
            nativeQuery = true
    )
    List<Question> findAnsweredByUrl(String url);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE question SET answered = NOT answered WHERE id = ?1",
            nativeQuery = true
    )
    void markAnsweredById(long id);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE question SET answer = ?3, answeredby_id = ?2 WHERE id = ?1",
            nativeQuery = true
    )
    void questionAnswer(long qid, long nid, String answer);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE question SET upvotes = ?1 WHERE id = ?2",
            nativeQuery = true
    )
    void updateUpvotes(int upvotes, long id);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE question SET question = ?3, changedby_id = ?2 WHERE id = ?1",
            nativeQuery = true
    )
    void questionEdit(long qid, long nid, String change);
}
