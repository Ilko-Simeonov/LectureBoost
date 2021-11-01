package nl.tudelft.oopp.demo.servercontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

public class QuestionControllerTest {
    private transient QuestionService questionService;
    private transient QuestionController questionController;
    private Question question;
    private List<Question> questions;

    @BeforeEach
    void setup() {
        questionService = mock(QuestionService.class);
        questionController = new QuestionController(questionService);
        question = new Question("?");
        questions = new ArrayList<>();
        questions.add(question);
    }

    @Test
    void save() {
        question.setUrl("url");
        Question q = new Question("?", "url");
        when(questionService.save(question)).thenReturn(q);
        String strQuestion = question.getQuestion();
        assertEquals(strQuestion, questionController.save(question, "url").getQuestion());
        assertEquals(question.getRoomUrl(), questionController.save(question, "url").getRoomUrl());
    }

    @Test
    void findById() {
        Optional<Question> test = Optional.empty();
        when(questionService.findById(1L)).thenReturn(Optional.empty());
        assertEquals(test, questionController.findById(1L));
    }

    @Test
    void findByUrl() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("?", "url"));
        questions.get(0).setUrl("url");
        when(questionService.findByUrl("url")).thenReturn(list);
        String s = questions.get(0).getRoomUrl();
        assertEquals(s, questionController.findByUrl("url").get(0).getRoomUrl());
    }

    @Test
    void findByUrlAndUser() {
        questions.get(0).setNicknameId(1);
        questions.get(0).setUrl("url");
        questions.add(new Question("???"));
        questions.get(1).setUrl("url");
        questions.get(1).setNicknameId(2);
        List<Question> list = new ArrayList<>();
        list.add(questions.get(0));
        when(questionService.findByUrlAndUserID("url", 1)).thenReturn(list);
        assertEquals(questions.get(0), questionController.findByUrlAndUser("url", 1).get(0));
    }

    @Test
    void findAll() {
        questions.get(0).setNicknameId(1);
        questions.get(0).setUrl("url");
        questions.add(new Question("???"));
        questions.get(1).setUrl("url");
        questions.get(1).setNicknameId(2);
        List<Question> list = new ArrayList<>();
        list.add(questions.get(0));
        list.add(questions.get(1));
        when(questionService.findAll()).thenReturn(list);
        assertEquals(questions.get(0), questionController.findAll().get(0));
        assertEquals(questions.get(1), questionController.findAll().get(1));
    }

    @Test
    void findAllByUpvotesDesc() {
        when(questionService.findAllByUpvotesDesc()).thenReturn(questions);
        assertEquals(questions.get(0), questionController.findAllByUpvotesDesc().get(0));
    }

    @Test
    void findByUrlOrdered() {
        questions.get(0).setUrl("url");
        when(questionService.findByUrlOrdered("url")).thenReturn(questions);
        Question q = new Question("?", "url");
        String newString = q.getRoomUrl();
        assertEquals(newString, questionController.findByUrlOrdered("url").get(0).getRoomUrl());
    }

    @Test
    void findAnswered() {
        List<Question> e = new ArrayList<>();
        when(questionService.findAnswered()).thenReturn(e);
        assertTrue(questionController.findAnswered().isEmpty());
    }

    @Test
    void getAnsweredCsv() {
        List<Question> e = new ArrayList<>();
        byte[] buff = {};
        ByteArrayInputStream b = new ByteArrayInputStream(buff);
        when(questionService.findAnswered()).thenReturn(e);
        when(questionService.convertToCsvByteArray(e)).thenReturn(b);
        assertNull(questionController.getAnsweredCsv().getBody().getFilename());
    }

    @Test
    void findAnsweredByUrl() {
        List<Question> q = new ArrayList<>();
        when(questionService.findAnsweredByUrl("url")).thenReturn(q);
        assertTrue(questionController.findAnsweredByUrl("url").isEmpty());
    }

    @Test
    void getAnsweredCsvByUrl() {
        List<Question> q = new ArrayList<>();
        when(questionService.findAnsweredByUrl("url")).thenReturn(q);
        byte[] buff = {};
        ByteArrayInputStream b = new ByteArrayInputStream(buff);
        when(questionService.convertToCsvByteArray(q)).thenReturn(b);
        assertNull(questionController.getAnsweredCsvByUrl("url").getBody().getFilename());
    }

    @Test
    void deleteById() {
        doThrow(new Successful()).when(questionService).deleteById(1L, 2L);

        assertThrows(Successful.class, () -> questionController.deleteById(1, 2));
    }

    @Test
    void upvoteById() {
        doThrow(new Successful()).when(questionService).upvoteById(1L, 2L);

        assertThrows(Successful.class, () -> questionController.upvoteById(1, 2));
    }

    @Test
    void markAnsweredById() {
        doThrow(new Successful()).when(questionService).markAnsweredById(1L, 2L);

        assertThrows(Successful.class, () -> questionController.markAnsweredById(1L, 2L));
    }

    @Test
    void hasUpvoted() {
        when(questionService.hasUpvoted(1, 2)).thenReturn(true);
        when(questionService.hasUpvoted(1, 3)).thenReturn(false);

        assertTrue(questionController.hasUpvoted(1, 2));
        assertFalse(questionController.hasUpvoted(1, 3));
    }
}