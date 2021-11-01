package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.oopp.demo.entities.Upvote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpvoteTest {
    Upvote upvote;

    @BeforeEach
    public void init() {
        upvote = new Upvote(12, 3, 6, true);
    }

    @Test
    public void contructorTest() {
        assertNotNull(upvote);

        upvote = new Upvote();
        assertNotNull(upvote);

        upvote = new Upvote(12, 3, false);
        assertNotNull(upvote);
    }

    @Test
    public void getterTest() {
        assertEquals(12, upvote.getId());
        assertEquals(3, upvote.getQuestionId());
        assertEquals(6, upvote.getNicknameId());
        assertTrue(upvote.isUpvoted());
    }

    @Test
    public void setterTest() {
        upvote.setUpvoted(false);
        assertFalse(upvote.isUpvoted());

        upvote.setUpvoted(true);
        assertTrue(upvote.isUpvoted());
    }

    @Test
    public void toStringTest() {
        String expected = "Upvote{id=12, questionId=3, nicknameId=6, upvoted=true}";
        assertEquals(expected, upvote.toString());
    }

    @Test
    void equals() {
        Upvote other = new Upvote(13, 3, 6, true);
        assertEquals(upvote, other);
    }

    @Test
    void notEquals() {
        Upvote other = new Upvote(13, 9, 6, true);
        assertNotEquals(upvote, other);

        other = new Upvote(13, 3, 9, true);
        assertNotEquals(upvote, other);

        other = new Upvote(13, 3, 6, false);
        assertNotEquals(upvote, other);
    }

    @Test
    void equalsSelf() {
        assertEquals(upvote, upvote);
    }

    @Test
    void equalsNull() {
        assertNotEquals(upvote, null);
    }

    @Test
    void equalsDifferentClass() {
        assertFalse(upvote.equals(upvote.getId()));
    }
}
