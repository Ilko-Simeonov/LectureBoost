package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.oopp.demo.entities.Poll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PollTest {
    public Poll poll;

    @BeforeEach
    public void init() {
        poll = new Poll(12, "testUrl", "PollTitle", "A", "B", null, null, "C", "D", true);
    }

    @Test
    public void testConstructor() {
        assertNotNull(poll);
    }

    @Test
    public void testEmptyConstructor() {
        assertNotNull(new Poll());
    }

    @Test
    void getId() {
        assertEquals(12, poll.getId());
    }

    @Test
    void getUrl() {
        assertEquals("testUrl", poll.getRoomUrl());
    }

    @Test
    void getTitle() {
        assertEquals("PollTitle", poll.getTitle());
    }

    @Test
    void getOption1() {
        assertEquals("A", poll.getOption1());
    }

    @Test
    void getOption2() {
        assertEquals("B", poll.getOption2());
    }

    @Test
    void getOption3() {
        assertEquals("C", poll.getOption3());
    }

    @Test
    void getOption4() {
        assertEquals("D", poll.getOption4());
    }

    @Test
    void getOption5() {
        assertNull(poll.getOption5());
    }

    @Test
    void getOption6() {
        assertNull(poll.getOption6());
    }

    @Test
    void isActive() {
        assertTrue(poll.isActive());
    }

    @Test
    public void testToString() {
        String s = "Poll{id=12, roomUrl='testUrl', title='PollTitle', "
                + "option1='A', option2='B', option3='C', "
                + "option4='D', option5='null', option6='null', "
                + "active='true'}";
        assertEquals(s, poll.toString());
    }

    @Test
    public void testAmountOfOptions() {
        assertEquals(4, poll.amountOfOptions());

        Poll p2 = new Poll(1, "u", "t", "1", "1", "1", "1", "1", "1", false);
        assertEquals(6, p2.amountOfOptions());

        Poll p3 = new Poll(1, "u", "t", null, null, null, null, null, null, false);
        assertEquals(0, p3.amountOfOptions());
    }


    @Test
    void equals() {
        Poll other = new Poll(29, "testUrl", "PollTitle", "A", "B", null, null, "C", "D", true);
        assertEquals(poll, other);
    }

    @Test
    void notEquals() {
        Poll other = new Poll(12, "notTestUrl", "PollTitle", "A", "B", null, null, "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "NotPollTitle", "A", "B", null, null, "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "notA", "B", null, null, "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "notB", null, null, "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "B", "3", null, "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "B", null, "4", "C", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "B", null, null, "notC", "D", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "B", null, null, "C", "notD", true);
        assertNotEquals(poll, other);

        other = new Poll(12, "testUrl", "PollTitle", "A", "B", null, null, "C", "D", false);
        assertNotEquals(poll, other);
    }

    @Test
    void equalsDifferentConstructor() {
        //It shouldn't matter in which option you place the answers,
        //e.g the options can be set in one poll in spot number 1, 2, 5 and 6
        //and in the other poll in 1, 2, 3, 4.
        //As long as the ORDER is the same, they should return to be equal.
        Poll other = new Poll(12, "testUrl", "PollTitle", "A", "B","C", "D", null, null, true);
        assertEquals(poll, other);
    }

    @Test
    void equalsSame() {
        assertEquals(poll, poll);
    }

    @Test
    void equalsNull() {
        assertFalse(poll.equals(null));
    }

    @Test
    void equalsDifferentClass() {
        assertFalse(poll.equals(poll.getTitle()));
    }

}
