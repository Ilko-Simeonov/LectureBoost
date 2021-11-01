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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollAnswer;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.PollAnswerRepository;
import nl.tudelft.oopp.demo.repositories.PollRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PollServiceTest {
    private transient PollService pollService;

    private transient PollRepository pollRepository;
    private transient PollAnswerRepository pollAnswerRepository;
    private transient NicknameRepository nicknameRepository;

    private Poll poll;
    private Poll poll2;
    private Poll poll3;
    private List<Poll> polls;

    @BeforeEach
    void setup() {
        pollRepository = mock(PollRepository.class);
        pollAnswerRepository = mock(PollAnswerRepository.class);
        nicknameRepository = mock(NicknameRepository.class);

        pollService = new PollService(
                pollRepository,
                pollAnswerRepository,
                nicknameRepository
        );

        poll = new Poll(1, "url", "title", "A", "B", "C", "D", null, null, true);
        poll2 = new Poll(2, "url2", "title2", "A", "B", "C", "D", null, null, true);
        poll3 = new Poll(3, "url", "title3", "A", "B", "C", "D", "E", null, false);

        polls = new ArrayList<>();
        polls.add(poll);
        polls.add(poll2);
        polls.add(poll3);
    }

    @Test
    void findAll() {
        when(pollRepository.findAll()).thenReturn(polls);

        assertEquals(polls, pollService.findAll());
    }

    @Test
    void save() {
        when(pollRepository.save(any(Poll.class))).thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(poll, pollService.save(poll));
    }

    @Test
    void findByUrl() {
        polls.remove(1);
        when(pollRepository.findByUrl("url")).thenReturn(polls);

        assertEquals(polls, pollService.findByUrl("url"));
    }

    @Test
    void findByUrlOrdered() {
        polls.remove(1);
        when(pollRepository.findByUrlOrdered("url")).thenReturn(polls);

        assertEquals(polls, pollService.findByUrlOrdered("url"));
    }

    @Test
    void findActiveByUrl() {
        polls.remove(1);
        polls.remove(1);
        when(pollRepository.findActiveByUrl("url")).thenReturn(polls);

        assertEquals(polls, pollService.findActiveByUrl("url"));
    }

    @Test
    void answerPollNew() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(1, 2)).thenReturn(Optional.empty());
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollAnswerRepository.save(any(PollAnswer.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(3, pollService.answerPoll(1, 3, 2));

        //Check whether the save method is actually called.
        when(pollAnswerRepository.save(any(PollAnswer.class))).thenThrow(new Successful());
        assertThrows(Successful.class, () -> pollService.answerPoll(1, 3, 2));
    }


    @Test
    void answerPollOverwrite() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(1, 2)).thenReturn(Optional.of(
                new PollAnswer(0, 1, 2, 3)
        ));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        assertEquals(4, pollService.answerPoll(1, 4, 2));

        //Check whether the updateById method is actually called.
        doThrow(new Successful()).when(pollAnswerRepository).updateById(0, 4);
        assertThrows(Successful.class, () -> pollService.answerPoll(1, 4, 2));
    }

    @Test
    void answerPollDne() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(8, 2)).thenReturn(Optional.empty());
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals(-1, pollService.answerPoll(8, 3, 2));
    }

    @Test
    void answerInactivePoll() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(3, 2)).thenReturn(Optional.empty());
        when(pollRepository.findById(3L)).thenReturn(Optional.of(poll3));

        assertEquals(-1, pollService.answerPoll(3, 3, 2));
    }

    @Test
    void answerIllegalAnswer() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(1, 2)).thenReturn(Optional.empty());
        when(pollRepository.findById(3L)).thenReturn(Optional.of(poll));

        assertEquals(-1, pollService.answerPoll(1, 5, 2));
    }


    @Test
    void hasAnswered() {
        when(pollAnswerRepository.findByPollIdAndNicknameId(1L, 2L)).thenReturn(
                Optional.of(new PollAnswer(0, 1, 2, 3))
        );
        when(pollAnswerRepository.findByPollIdAndNicknameId(1L, 3L)).thenReturn(
                Optional.empty()
        );
        when(pollAnswerRepository.findByPollIdAndNicknameId(2L, 2L)).thenReturn(
                Optional.empty()
        );


        assertTrue(pollService.hasAnswered(1, 2));
        assertFalse(pollService.hasAnswered(1, 3));
        assertFalse(pollService.hasAnswered(2, 2));
    }

    @Test
    void findAllAnswers() {
        List<PollAnswer> pollAnswers = new ArrayList<>();
        pollAnswers.add(new PollAnswer(0, 1, 2, 3));
        pollAnswers.add(new PollAnswer(1, 1, 3, 2));

        when(pollAnswerRepository.findAll()).thenReturn(pollAnswers);

        assertEquals(pollAnswers, pollService.findAllAnswers());
    }

    @Test
    void getResults() {
        when(pollRepository.findById(3L)).thenReturn(Optional.of(poll3));
        when(pollAnswerRepository.getAmountOfAnswers(3L, 1)).thenReturn(3);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 2)).thenReturn(6);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 3)).thenReturn(1);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 4)).thenReturn(2);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 5)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 6))
                .thenThrow(new IllegalArgumentException());

        List<Integer> results = new ArrayList<>(Arrays.asList(
                25, 50, 8, 17, 0
        ));

        assertEquals(results, pollService.getResults(3));
    }

    @Test
    void getResultsNoAnswers() {
        when(pollRepository.findById(3L)).thenReturn(Optional.of(poll3));
        when(pollAnswerRepository.getAmountOfAnswers(3L, 1)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 2)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 3)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 4)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 5)).thenReturn(0);
        when(pollAnswerRepository.getAmountOfAnswers(3L, 6))
                .thenThrow(new IllegalArgumentException());

        List<Integer> results = new ArrayList<>(Arrays.asList(
                0, 0, 0, 0, 0
        ));

        assertEquals(results, pollService.getResults(3));
    }

    @Test
    void getResultsPollDne() {
        when(pollRepository.findById(3L)).thenReturn(Optional.empty());

        assertNull(pollService.getResults(3));
    }

    @Test
    void deleteByIdAuthorized1() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollRepository).deleteById(1L);

        assertThrows(Successful.class, () -> pollService.deleteById(1, 2));
    }

    @Test
    void deleteByIdAuthorized2() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollAnswerRepository).deleteByPid(1L);

        assertThrows(Successful.class, () -> pollService.deleteById(1, 2));
    }

    @Test
    void deleteByIdNotAuthorized1() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", false)
        ));

        doThrow(new Successful()).when(pollAnswerRepository).deleteByPid(1L);

        try {
            pollService.deleteById(1, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }

    @Test
    void deleteByIdNotAuthorized2() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "notUrl", true)
        ));

        doThrow(new Successful()).when(pollAnswerRepository).deleteByPid(1L);

        try {
            pollService.deleteById(1, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }

    @Test
    void deletPollDne() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollAnswerRepository).deleteByPid(1L);

        try {
            pollService.deleteById(1, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }

    @Test
    void setPollActiveAuthorized() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollRepository).setActive(1L, true);

        assertThrows(Successful.class, () -> pollService.setPollActive(1, true, 2));
    }

    @Test
    void setPollInactiveAuthorized() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollRepository).setActive(1L, false);

        assertThrows(Successful.class, () -> pollService.setPollActive(1, false, 2));
    }

    @Test
    void setPollActiveNotAuthorized1() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", false)
        ));

        doThrow(new Successful()).when(pollRepository).setActive(1L, true);

        try {
            pollService.setPollActive(1, true, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }

    @Test
    void setPollActiveNotAuthorized2() {
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "notUrl", true)
        ));

        doThrow(new Successful()).when(pollRepository).setActive(1L, true);

        try {
            pollService.setPollActive(1, true, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }

    @Test
    void setActivePollDne() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("mod", "url", true)
        ));

        doThrow(new Successful()).when(pollRepository).setActive(1L, true);

        try {
            pollService.setPollActive(1, true, 2);
        } catch (Successful s) {
            //If Successful is thrown, then a delete method was called, so it should fail.
            fail();
        }
    }
}
