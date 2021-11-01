package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class NicknameTest {

    Nickname test;

    @BeforeEach
    void setUp() {
        test = new Nickname("Jeff", "url", false);
    }

    @Test
    void testEmptyConstructor() {
        assertNotNull(new Nickname());
    }

    @Test
    void getId() {
        assertEquals(0, test.getId());
    }

    @Test
    void getName() {
        assertEquals("Jeff", test.getName());
    }

    @Test
    void getRoomUrl() {
        assertEquals("url", test.getRoomUrl());
    }

    @Test
    void setName() {
        test.setName("Rick");
        assertEquals("Rick", test.getName());
    }

    @Test
    void setRoomUrl() {
        test.setRoomUrl("setUrl");
        assertEquals("setUrl", test.getRoomUrl());
    }

    @Test
    void isModerator() {
        assertFalse(test.isModerator());
    }

    @Test
    void isActive() {
        assertTrue(test.isActive());
    }

    @Test
    void setActive() {
        test.setActive(false);
        assertFalse(test.isActive());
    }

    @Test
    void setModerator() {
        test.setModerator(true);
        assertTrue(test.isModerator());
    }

    @Test
    void testToString() {
        String e = "Nickname{id=0, name='Jeff', roomUrl='url', "
                + "moderator=false, active=true, muted=false}";
        String a = test.toString();
        assertEquals(e, a);
    }

    @Test
    void equals() {
        Nickname other = new Nickname("Jeff", "url", false);
        assertEquals(test, other);
    }

    @Test
    void equalsSame() {
        assertEquals(test, test);
    }

    @Test
    void equalsNot() {
        Nickname other = new Nickname("NotJeff", "url", false);
        assertNotEquals(test, other);

        other = new Nickname("Jeff", "notUrl", false);
        assertNotEquals(test, other);

        other = new Nickname("Jeff", "url", true);
        assertNotEquals(test, other);

        other.setModerator(false);
        assertEquals(test, other);

        other.setActive(false);
        assertNotEquals(test, other);
    }

    @Test
    void equalsNull() {
        assertFalse(test.equals(null));
    }

    @Test
    void equalsOtherClass() {
        assertFalse(test.equals(test.getRoomUrl()));
    }
}