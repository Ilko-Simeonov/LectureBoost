package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.Upvote;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UpvoteRepository;
import nl.tudelft.oopp.demo.util.CsvUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.server.ResponseStatusException;


public class QuestionServiceTest {
    private transient QuestionService questionService;

    private transient QuestionRepository questionRepository;
    private transient NicknameRepository nicknameRepository;
    private transient UpvoteRepository upvoteRepository;
    private transient RoomRepository roomRepository;

    private Question question;
    private Question question2;
    private Question question3;
    private List<Question> questions;

    private Room room;
    private Nickname nickname;

    @BeforeEach
    void setup() {
        questionRepository = mock(QuestionRepository.class);
        nicknameRepository = mock(NicknameRepository.class);
        upvoteRepository = mock(UpvoteRepository.class);
        roomRepository = mock(RoomRepository.class);

        questionService = new QuestionService(
                questionRepository,
                nicknameRepository,
                upvoteRepository,
                roomRepository);

        question = new Question(1, "url", "Question", 50, 2, false);
        question2 = new Question(2, "url2", "Question2", 52, 4, true);
        question3 = new Question(3, "url", "Question3", 30, 3, false);

        questions = new ArrayList<>();
        questions.add(question);
        questions.add(question2);
        questions.add(question3);

        long start = 1617800400000L; //07-APR-2021 @ 15:00:00
        long end = start + 3600000; //start + 1hr
        room = new Room("Room", "url", true, "pwd", start, end, 30);

        nickname = new Nickname("name", "url", false);
    }

    @Test
    void save() {
        when(questionRepository.save(question)).thenReturn(question);
        when(nicknameRepository.findById(question.getNicknameId()))
                .thenReturn(Optional.of(nickname));
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.of(room));

        assertEquals(question, questionService.save(question));
    }

    @Test
    void saveNull() {
        when(questionRepository.save(null)).thenThrow(new IllegalArgumentException());
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.of(room));

        assertThrows(NullPointerException.class, () -> questionService.save(null));
    }

    @Test
    void saveRoomDne() {
        when(questionRepository.save(question)).thenReturn(question);
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.empty());

        assertNull(questionService.save(question));
    }

    @Test
    void saveClosedRoom() {
        room.setOpen(false);

        when(questionRepository.save(question)).thenReturn(question);
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.of(room));

        assertNull(questionService.save(question));
    }

    @Test
    void findAll() {
        questions.add(question2);
        when(questionRepository.findAll()).thenReturn(questions);
        assertEquals(questions, questionService.findAll());
    }

    @Test
    void findById() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        assertEquals(Optional.of(question), questionService.findById(1L));
    }

    @Test
    void findById2() {
        when(questionRepository.findById(2L)).thenReturn(Optional.empty());
        assertTrue(questionService.findById(2L).isEmpty());
    }

    @Test
    void findByUrl() {
        List<Question> urlQuestions = new ArrayList<>();
        urlQuestions.add(question);
        urlQuestions.add(question3);

        when(questionRepository.findByUrl("url")).thenReturn(urlQuestions);

        assertEquals(urlQuestions, questionService.findByUrl("url"));
    }

    @Test
    void findByUrlEmpty() {
        List<Question> urlQuestions = new ArrayList<>();

        when(questionRepository.findByUrl("urlDne")).thenReturn(urlQuestions);

        assertEquals(urlQuestions, questionService.findByUrl("urlDne"));
    }

    @Test
    void findByUrlAndUserID() {
        List<Question> urlIdQuestions = new ArrayList<>();
        urlIdQuestions.add(question);

        when(questionRepository.findByUrlAndUserID("url", 2)).thenReturn(urlIdQuestions);

        assertEquals(urlIdQuestions, questionService.findByUrlAndUserID("url", 2));
    }

    @Test
    void deleteOwnQuestion() {
        List<Question> delQuestions = new ArrayList<>();
        delQuestions.add(question);

        Nickname nickname = new Nickname("name", "url", false);

        when(questionRepository.findAllById(List.of(1L))).thenReturn(delQuestions);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).deleteAll(delQuestions);

        assertThrows(Successful.class, () -> questionService.deleteById(1, 2));
    }

    @Test
    void deleteQuestionModerator() {
        List<Question> delQuestions = new ArrayList<>();
        delQuestions.add(question);

        Nickname nickname = new Nickname("name", "url", true);

        when(questionRepository.findAllById(List.of(1L))).thenReturn(delQuestions);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(3L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).deleteAll(delQuestions);

        assertThrows(Successful.class, () -> questionService.deleteById(1, 3));
    }

    @Test
    void deleteQuestionNotAuthorized() {
        List<Question> delQuestions = new ArrayList<>();
        delQuestions.add(question);

        Nickname nickname = new Nickname("name", "url", false);

        when(questionRepository.findAllById(List.of(1L))).thenReturn(delQuestions);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(3L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).deleteAll(delQuestions);

        assertThrows(ResponseStatusException.class, () -> questionService.deleteById(1, 3));
    }

    @Test
    void upvoteById() {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(upvoteRepository.findByQidNid(
                question.getId(),
                nickname.getId())
        ).thenReturn(Optional.empty());

        when(upvoteRepository.save(any(Upvote.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        when(upvoteRepository.countUpvotesByQid(question.getId())).thenReturn(12);

        doThrow(new Successful()).when(questionRepository).updateUpvotes(12, question.getId());

        assertThrows(Successful.class, () -> questionService.upvoteById(
                question.getId(),
                nickname.getId()));
    }

    @Test
    void upvoteByIdSaveCheck() {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(upvoteRepository.findByQidNid(
                question.getId(),
                nickname.getId())
        ).thenReturn(Optional.empty());

        when(upvoteRepository.save(new Upvote(
                question.getId(),
                nickname.getId(),
                true
        ))).thenThrow(new Successful());

        assertThrows(Successful.class, () -> questionService.upvoteById(
                question.getId(),
                nickname.getId()));
    }

    @Test
    void upvoteNull() {
        when(questionRepository.findById(7L)).thenReturn(Optional.empty());
        doThrow(new Successful()).when(questionRepository).upvoteById(1L);

        try {
            questionService.upvoteById(7, 2);
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void upvoteAnsweredQuestion() {
        when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        doThrow(new Successful()).when(questionRepository).upvoteById(2L);

        try {
            questionService.upvoteById(2, 3);
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void changeUpvoteToTrue() {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(upvoteRepository.findByQidNid(
                question.getId(),
                nickname.getId())
        ).thenReturn(Optional.of(new Upvote(question.getId(), nickname.getId(), false)));

        doThrow(new Successful()).when(upvoteRepository).upvote(
                true, nickname.getId(), question.getId()
        );

        assertThrows(Successful.class, () -> questionService.upvoteById(
                question.getId(),
                nickname.getId()));
    }

    @Test
    void changeUpvoteToFalse() {
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(upvoteRepository.findByQidNid(
                question.getId(),
                nickname.getId())
        ).thenReturn(Optional.of(new Upvote(question.getId(), nickname.getId(), true)));

        doThrow(new Successful()).when(upvoteRepository).upvote(
                false, nickname.getId(), question.getId()
        );

        assertThrows(Successful.class, () -> questionService.upvoteById(
                question.getId(),
                nickname.getId()));
    }

    @Test
    void findAllByUpvotesDesc() {
        List<Question> questionsSorted = new ArrayList<>();
        questionsSorted.add(question2);
        questionsSorted.add(question);
        questionsSorted.add(question3);

        when(questionRepository.findAllByOrderByUpvotesDesc()).thenReturn(questionsSorted);

        assertEquals(questionsSorted, questionService.findAllByUpvotesDesc());
    }

    @Test
    void findByUrlOrdered() {
        List<Question> questionsSorted = new ArrayList<>();
        questionsSorted.add(question);
        questionsSorted.add(question3);

        when(questionRepository.findByUrlOrdered("url")).thenReturn(questionsSorted);

        assertEquals(questionsSorted, questionService.findByUrlOrdered("url"));
    }

    @Test
    void findAnswered() {
        List<Question> answered = new ArrayList<>();
        answered.add(question2);

        when(questionRepository.findAnswered()).thenReturn(answered);

        assertEquals(answered, questionService.findAnswered());
    }

    @Test
    void findAnsweredByUrl() {
        List<Question> answered = new ArrayList<>();
        answered.add(question2);

        when(questionRepository.findAnsweredByUrl("url2")).thenReturn(answered);

        assertEquals(answered, questionService.findAnsweredByUrl("url2"));
    }

    @Test
    void convertToCsvByteArray() {
        CsvUtil<Question> csvUtil = new CsvUtil<>();
        ByteArrayInputStream expected = csvUtil.toCsvInputStream(
                Arrays.asList("id", "roomUrl", "question", "upvotes", "answered", "nicknameId"),
                questions);

        ByteArrayInputStream actual = questionService.convertToCsvByteArray(questions);

        while (expected.available() > 0) {
            int expectedByte = expected.read();
            int actualByte = actual.read();
            if (expectedByte != actualByte) {
                fail();
            }
        }
    }

    @Test
    void markAnsweredOwnQuestion() {
        Nickname nickname = new Nickname("name", "url", false);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).markAnsweredById(1L);

        assertThrows(Successful.class, () -> questionService.markAnsweredById(1, 2));
    }
    
    @Test
    void markAnsweredQuestionModerator() {
        Nickname nickname = new Nickname("name", "url", true);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(3L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).markAnsweredById(1);

        assertThrows(Successful.class, () -> questionService.markAnsweredById(1, 3));
    }

    @Test
    void markAnsweredNonExistingQuestion() {
        Nickname nickname = new Nickname("name", "url", true);

        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        when(nicknameRepository.findById(3L)).thenReturn(Optional.of(nickname));
        doThrow(new Successful()).when(questionRepository).markAnsweredById(1);

        assertThrows(ResponseStatusException.class, () -> questionService.markAnsweredById(1, 3));
    }

    @Test
    void markAnsweredQuestionNonExistingNickname() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(nicknameRepository.findById(3L)).thenReturn(Optional.empty());
        doThrow(new Successful()).when(questionRepository).markAnsweredById(1);

        assertThrows(ResponseStatusException.class, () -> questionService.markAnsweredById(1, 3));
    }

    @Test
    void hasUpvoted() {
        when(upvoteRepository.findByQidNid(1L, 2L)).thenReturn(Optional.of(
                new Upvote(1, 2, true)
        ));
        when(upvoteRepository.findByQidNid(1L, 3L)).thenReturn(Optional.of(
                new Upvote(1, 3, false)
        ));
        when(upvoteRepository.findByQidNid(1L, 4L)).thenReturn(Optional.empty());

        assertTrue(questionService.hasUpvoted(1, 2));
        assertFalse(questionService.hasUpvoted(1, 3));
        assertFalse(questionService.hasUpvoted(1, 4));
    }

}
