package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NicknameTest {
    Nickname nickname;

    @BeforeEach
    public void init() {
        nickname = new Nickname(1, "Joey", "u3Sno6", true, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(nickname);
    }

    @Test
    public void testGetters() {
        assertEquals(1, nickname.getId());
        assertEquals("Joey", nickname.getName());
        assertEquals("u3Sno6", nickname.getRoomUrl());
        assertTrue(nickname.isModerator());
    }
}
