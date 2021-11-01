package nl.tudelft.oopp.demo.services;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.Upvote;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UpvoteRepository;
import nl.tudelft.oopp.demo.util.CsvUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final NicknameRepository nicknameRepository;
    private final UpvoteRepository upvoteRepository;
    private final RoomRepository roomRepository;

    /**
     * Instantiates a new Question service.
     *
     * @param questionRepository the question repository
     * @param nicknameRepository the nickname repository
     * @param upvoteRepository   the upvote repository
     * @param roomRepository     the room repository
     */
    @Autowired
    public QuestionService(@Qualifier("QuestionRepository") QuestionRepository questionRepository,
                           @Qualifier("NicknameRepository") NicknameRepository nicknameRepository,
                           @Qualifier("UpvoteRepository") UpvoteRepository upvoteRepository,
                           @Qualifier("RoomRepository") RoomRepository roomRepository) {
        this.questionRepository = questionRepository;
        this.nicknameRepository = nicknameRepository;
        this.roomRepository = roomRepository;
        this.upvoteRepository = upvoteRepository;
    }

    /**
     * Saves question iff the room exists and is open.
     *
     * @param question the question
     * @return the question
     */
    public Question save(Question question) {
        Optional<Room> r = roomRepository.findByUrl(question.getRoomUrl());
        if (r.isEmpty() || !r.get().isOpen()) {
            System.out.println(1);
            return null;
        }

        Optional<Nickname> nicknameOptional = nicknameRepository.findById(question.getNicknameId());

        if (nicknameOptional.isEmpty()) {
            System.out.println(2);

            return null;
        }

        Nickname nickname = nicknameOptional.get();

        if (nickname.isMuted()) {
            System.out.println(3);

            return null;
        }

        return questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    /**
     * Finds all question for a url.
     *
     * @param url the url
     * @return list of questions for the url
     */
    public List<Question> findByUrl(String url) {
        return questionRepository.findByUrl(url);
    }

    /**
     * Finds all question for a url and user.
     *
     * @param url the url
     * @param nicknameid the nickname
     * @return list of questions for the url and specific user
     */
    public List<Question> findByUrlAndUserID(String url, int nicknameid) {
        return questionRepository.findByUrlAndUserID(url, nicknameid);
    }

    /**
     * Delete a question by id if the person deleting is authorized.
     * A person is authorized if they are the person who asked the question or a moderator.
     *
     * @param qid the question id
     * @param nid the nickname id
     */
    public void deleteById(long qid, long nid) {
        if (isAuthorized(qid, nid)) {
            questionRepository.deleteAll(questionRepository.findAllById(List.of(qid)));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    /**
     * Upvote a question by id.
     * Checks whether the question exists.
     * If the question is marked as answered, does NOT upvote.
     *
     * @param qid the question_id
     * @param nid the nickname_id
     */
    public void upvoteById(long qid, long nid) {
        Optional<Question> optional = questionRepository.findById(qid);

        if (optional.isEmpty()) {
            //If there does not exist a question with that id, return.
            return;
        }

        Question q = optional.get();

        if (q.isAnswered()) {
            //If there exists a question with that id, but it is already marked as answered, return.
            return;
        }

        Optional<Upvote> upvoteOptional = upvoteRepository.findByQidNid(qid, nid);

        if (upvoteOptional.isEmpty()) {
            upvoteRepository.save(new Upvote(qid, nid, true));
        } else {

            Upvote upvote = upvoteOptional.get();

            if (upvote.isUpvoted()) {
                upvoteRepository.upvote(false, nid, qid);
            } else {
                upvoteRepository.upvote(true, nid, qid);
            }
        }

        int upvotes = upvoteRepository.countUpvotesByQid(qid);
        questionRepository.updateUpvotes(upvotes, qid);
    }

    public List<Question> findAllByUpvotesDesc() {
        return questionRepository.findAllByOrderByUpvotesDesc();
    }


    public List<Question> findByUrlOrdered(String url) {
        return questionRepository.findByUrlOrdered(url);
    }

    public List<Question> findAnswered() {
        return questionRepository.findAnswered();
    }

    /**
     * Method returns the array as ByteArrayInputStream for the list of questions that are answered.
     * A ByteArrayInputStream contains an internal buffer that contains
     * bytes that may be read from the stream.
     * An internal counter keeps track of the next byte to be supplied by the read method.
     * @param questions the list of questions answered
     * @return  returns the array as ByteArrayInputStream.
     */
    public ByteArrayInputStream convertToCsvByteArray(List<Question> questions) {
        CsvUtil<Question> csvUtil = new CsvUtil<>();
        return csvUtil.toCsvInputStream(
                Arrays.asList("id", "roomUrl", "question", "upvotes", "answered", "nicknameId"),
                questions);
    }

    public List<Question> findAnsweredByUrl(String url) {
        return questionRepository.findAnsweredByUrl(url);
    }

    /**
     * Mark a question as answered by id if the person deleting is authorized.
     * A person is authorized if they are the person who asked the question or a moderator.
     *
     * @param qid the question id
     * @param nid the nickname id
     */
    public void markAnsweredById(long qid, long nid) {
        if (isAuthorized(qid, nid)) {
            System.out.println("Yes.. marked");
            questionRepository.markAnsweredById(qid);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    /**
     * Check if a person is authorized to delete/edit a question or mark it as answered.
     * A person is authorized if they are the person who asked the question or a moderator.
     *
     * @param qid the question id
     * @param nid the nickname id
     * @return - true iff the person is a moderator or the one that asked the question.
     */
    public boolean isAuthorized(long qid, long nid) {
        Optional<Question> optionalQuestion = questionRepository.findById(qid);
        if (optionalQuestion.isEmpty()) {
            return false;
        }
        Question question = optionalQuestion.get();

        Optional<Nickname> optionalNickname = nicknameRepository.findById(nid);
        if (optionalNickname.isEmpty()) {
            return false;
        }
        Nickname nickname = optionalNickname.get();

        return (nid == question.getNicknameId() || nickname.isModerator());
    }

    /**
     * Answer a question by id if the person answering it is authorized.
     * A person is authorized if they are a moderator.
     * @param qid id of the asked question
     * @param nid nickname id of the answering person
     * @param answer String of answer from the moderator.
     */
    public void questionAnswer(long qid, long nid, String answer) {
        if (isAuthorizedAnswer(qid, nid)) {
            questionRepository.questionAnswer(qid, nid, answer);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    /**
     * Change a question by id if the person changing it is authorized.
     * A person is authorized if they are a moderator.
     * @param qid id of the asked question
     * @param nid nickname id of the answering person
     * @param change String of edited question from the moderator.
     */
    public void questionEdit(long qid, long nid, String change) {
        if (isAuthorizedAnswer(qid, nid)) {
            questionRepository.questionEdit(qid, nid, change);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    /**
     * Check if a person is authorized to answer a question.
     * A person is authorized if they are a moderator.
     *
     * @param qid the question id
     * @param nid the nickname id
     * @return - true iff the person is a moderator.
     */
    public boolean isAuthorizedAnswer(long qid, long nid) {
        Optional<Question> optionalQuestion = questionRepository.findById(qid);
        if (optionalQuestion.isEmpty()) {
            return false;
        }
        Question question = optionalQuestion.get();

        Optional<Nickname> optionalNickname = nicknameRepository.findById(nid);
        if (optionalNickname.isEmpty()) {
            return false;
        }
        Nickname nickname = optionalNickname.get();

        return (nid == question.getNicknameId()
                || (nickname.isModerator() && nickname.getRoomUrl().equals(question.getRoomUrl())));
    }

    /**
     * Checks whether a certain person has upvoted a certain question.
     *
     * @param qid the question_id
     * @param nid the nickname_id
     * @return whether the person has upvoted the question
     */
    public boolean hasUpvoted(long qid, long nid) {
        Optional<Upvote> optionalUpvote = upvoteRepository.findByQidNid(qid, nid);

        if (optionalUpvote.isEmpty()) {
            return false;
        }

        Upvote upvote = optionalUpvote.get();

        return upvote.isUpvoted();
    }
}
