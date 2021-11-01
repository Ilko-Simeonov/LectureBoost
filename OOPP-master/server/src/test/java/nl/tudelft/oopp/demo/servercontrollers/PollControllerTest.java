package nl.tudelft.oopp.demo.servercontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollAnswer;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.services.PollService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

public class PollControllerTest {
    private transient PollController pollController;
    private transient PollService pollService;

    private Poll poll;
    private Poll poll2;

    private List<Poll> polls;

    @BeforeEach
    void setup() {
        pollService = Mockito.mock(PollService.class);
        pollController = new PollController(pollService);

        poll = new Poll(1, "url", "title", "A", "B", "C", "D", null, null, true);
        poll2 = new Poll(2, "url2", "title2", "A", "B", "C", "D", null, null, true);
        
        polls = new ArrayList<>();
        polls.add(poll);
        polls.add(poll2);
    }

    @Test
    void createPoll() {
        when(pollService.save(any(Poll.class))).thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(poll, pollController.createPoll(poll));
    }

    @Test
    void findAll() {
        when(pollService.findAll()).thenReturn(polls);
        
        assertEquals(polls, pollController.findAll());
    }

    @Test
    void findByUrl() {
        polls.remove(1);
        when(pollService.findByUrl("url")).thenReturn(polls);
        
        assertEquals(polls, pollController.findByUrl("url"));
    }

    @Test
    void findByUrlOrdered() {
        polls.remove(1);
        when(pollService.findByUrlOrdered("url")).thenReturn(polls);

        assertEquals(polls, pollController.findByUrlOrdered("url"));
    }

    @Test
    void findActiveByUrl() {
        polls.remove(1);
        when(pollService.findActiveByUrl("url")).thenReturn(polls);

        assertEquals(polls, pollController.findActiveByUrl("url"));
    }

    @Test
    void answerPoll() {
        when(pollService.answerPoll(1, 3, 5)).thenReturn(3);
        assertEquals(3, pollController.answerPoll(1, 3, 5));
    }

    @Test
    void hasAnswered() {
        when(pollService.hasAnswered(1, 5)).thenReturn(true);
        when(pollService.hasAnswered(1, 4)).thenReturn(false);
        
        assertTrue(pollController.hasAnswered(1, 5));
        assertFalse(pollController.hasAnswered(1, 4));
    }

    @Test
    void getAnswers() {
        List<PollAnswer> answers = new ArrayList<>();
        answers.add(new PollAnswer(0, 1, 5, 3));
        answers.add(new PollAnswer(1, 1, 7, 2));

        when(pollService.findAllAnswers()).thenReturn(answers);
        
        assertEquals(answers, pollController.getAnswers());
    }

    @Test
    void getResults() {
        List<Integer> results = new ArrayList<>();
        results.add(0);
        results.add(67);
        results.add(33);
        
        when(pollService.getResults(1)).thenReturn(results);
        
        assertEquals(results, pollController.getResults(1));
    }

    @Test
    void deleteById() {
        doThrow(new Successful()).when(pollService).deleteById(1, 2);

        assertThrows(Successful.class, () -> pollController.deleteById(1, 2));
    }

    @Test
    void openPoll() {
        doThrow(new Successful()).when(pollService).setPollActive(1, true,2);

        assertThrows(Successful.class, () -> pollController.openPoll(1, 2));
    }

    @Test
    void closePoll() {
        doThrow(new Successful()).when(pollService).setPollActive(1, false,2);

        assertThrows(Successful.class, () -> pollController.closePoll(1, 2));
    }
}
