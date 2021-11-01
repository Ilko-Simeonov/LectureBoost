package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PollTest {
    public Poll poll;

    @BeforeEach
    public void init() {
        poll = new Poll(12, "testUrl", "PollTitle", "A", "B", null, null, "C", "D", true, true);
    }

    @Test
    public void testConstructorAndGetters() {
        assertNotNull(poll);
        assertEquals(12, poll.getId());
        assertEquals("testUrl", poll.getRoomUrl());
        assertEquals("PollTitle", poll.getTitle());
        assertEquals("A", poll.getOption1());
        assertEquals("B", poll.getOption2());
        assertEquals("C", poll.getOption3());
        assertEquals("D", poll.getOption4());
        assertNull(poll.getOption5());
        assertNull(poll.getOption6());
        assertTrue(poll.isActive());
        assertTrue(poll.isAnswered());
    }

    @Test
    public void testToString() {
        String s = "Poll{id=12, roomUrl='testUrl', title='PollTitle', "
                + "option1='A', option2='B', option3='C', "
                + "option4='D', option5='null', option6='null', "
                + "active=true, answered=true}";
        assertEquals(s, poll.toString());
    }

    @Test
    public void testAmountOfOptions() {
        assertEquals(4, poll.amountOfOptions());

        Poll p2 = new Poll(1, "u", "t", "1", "1", "1", "1", "1", "1", false, true);
        assertEquals(6, p2.amountOfOptions());

        Poll p3 = new Poll(1, "u", "t", null, null, null, null, null, null, false, true);
        assertEquals(0, p3.amountOfOptions());
    }


}
