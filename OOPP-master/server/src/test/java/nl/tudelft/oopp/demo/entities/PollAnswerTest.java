package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PollAnswerTest {
    PollAnswer pollAnswer;

    @BeforeEach
    void setUp() {
        pollAnswer = new PollAnswer(2, 4, 1, 3);
    }

    @Test
    void testConstructor() {
        assertNotNull(pollAnswer);
    }

    @Test
    void testEmptyConstructor() {
        assertNotNull(new PollAnswer());
    }

    @Test
    void getId() {
        assertEquals(2, pollAnswer.getId());
    }

    @Test
    void getPollId() {
        assertEquals(4, pollAnswer.getPollId());
    }

    @Test
    void getNicknameId() {
        assertEquals(1, pollAnswer.getNicknameId());
    }

    @Test
    void getAnswer() {
        assertEquals(3, pollAnswer.getAnswer());
    }

    @Test
    void testToString() {
        String s = "PollAnswer{id=2, pollId='4', nicknameId='1', answer=3}";
        assertEquals(s, pollAnswer.toString());
    }

    @Test
    void equals() {
        //Different id
        PollAnswer other = new PollAnswer(7, 4, 1, 3);
        assertEquals(pollAnswer, other);
    }

    @Test
    void notEquals() {
        PollAnswer other = new PollAnswer(2, 7, 1, 3);
        assertNotEquals(pollAnswer, other);

        other = new PollAnswer(2, 4, 7, 3);
        assertNotEquals(pollAnswer, other);

        other = new PollAnswer(2, 4, 1, 6);
        assertNotEquals(pollAnswer, other);
    }

    @Test
    void equalsSelf() {
        assertEquals(pollAnswer, pollAnswer);
    }

    @Test
    void equalsNull() {
        assertNotEquals(pollAnswer, null);
    }

    @Test
    void equalsDifferentClass() {
        assertFalse(pollAnswer.equals(pollAnswer.getId()));
    }
}
