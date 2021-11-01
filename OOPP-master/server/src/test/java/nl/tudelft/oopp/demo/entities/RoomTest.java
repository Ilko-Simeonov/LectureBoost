package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.time.ZonedDateTime;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class RoomTest {

    Room room;
    long start;
    long end;

    @BeforeEach
    void setUp() {
        long currentTime = 1617800400000L; //07-APR-2021 @ 15:00:00
        start = currentTime;
        end = currentTime + 3600000; //start + 1hr 
        room = new Room("Room", "url", true, "pwd", start, end, 30);
    }

    @Test
    void emptyConstructor() {
        Room testRoom = new Room();
        assertNotNull(testRoom);
    }

    @Test
    void getName() {
        assertEquals("Room", room.getName());
    }

    @Test
    void getUrl() {
        assertEquals("url", room.getUrl());
    }

    @Test
    void getStartTime() {
        long time = start;
        assertEquals(time, room.getStartTime());
    }

    @Test
    void getEndTime() {
        long time = end;
        assertEquals(time, room.getEndTime());
    }

    @Test
    void isOpen() {
        assertTrue(room.isOpen());
    }

    @Test
    void getModeratorPassword() {
        assertEquals("pwd", room.getModeratorPassword());
    }

    @Test
    void getQuestionCooldown() {
        assertEquals(30, room.getQuestionCooldown());
    }

    @Test
    void setQuestionCooldown() {
        room.setQuestionCooldown(20);
        assertEquals(20, room.getQuestionCooldown());
    }

    @Test
    void setOpen() {
        room.setOpen(false);
        assertFalse(room.isOpen());
    }

    @Test
    void setModeratorPassword() {
        room.setModeratorPassword("newPWD");
        assertEquals("newPWD", room.getModeratorPassword());
    }

    @Test
    void setUrl() {
        room.setUrl("setUrl");
        assertEquals("setUrl", room.getUrl());
    }

    @Test
    void setStartTime() {
        long currentTime = System.currentTimeMillis();
        room.setStartTime(currentTime);
        assertEquals(currentTime, room.getStartTime());
    }

    @Test
    void setEndTime() {
        long currentTime = System.currentTimeMillis();
        room.setEndTime(currentTime);
        assertEquals(currentTime, room.getEndTime());
    }

    @Test
    void equals() {
        Room other = new Room("Room", "url", true, "pwd", start, end, 30);
        assertEquals(room, other);
    }

    @Test
    void notEquals() {
        Room other = new Room("notRoom", "url", true, "pwd", start, end, 30);
        assertNotEquals(room, other);

        other = new Room("Room", "notUrl", true, "pwd", start, end, 30);
        assertNotEquals(room, other);

        other = new Room("Room", "url", false, "pwd", start, end, 30);
        assertNotEquals(room, other);

        other = new Room("Room", "url", true, "notPwd", start, end, 30);
        assertNotEquals(room, other);

        other = new Room("Room", "url", true, "pwd", end, end, 30);
        assertNotEquals(room, other);

        other = new Room("Room", "url", true, "pwd", start, start, 30);
        assertNotEquals(room, other);
    }

    @Test
    void equalsSelf() {
        assertEquals(room, room);
    }

    @Test
    void equalsDifferentClass() {
        assertFalse(room.equals(room.getStartTime()));
    }

    @Test
    void testToString() {
        String test = "Room{roomUrl='url', name='Room', open='true', "
                + "starttime='07-APR-2021 @ 15:00', endtime='07-APR-2021 @ 16:00'}";
        assertEquals(test, room.toString());
    }

    /**
     * Test calendar to string.
     */
    @Test
    void testCalendarToString() {
        Calendar c = Calendar.getInstance();
        c.set(2021, 0, 2, 1, 5, 0);

        String expected = "02-JAN-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        //Test for every month
        c.set(Calendar.MONTH, 1);
        expected = "02-FEB-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 2);
        expected = "02-MAR-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 3);
        expected = "02-APR-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 4);
        expected = "02-MAY-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 5);
        expected = "02-JUN-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 6);
        expected = "02-JUL-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 7);
        expected = "02-AUG-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 8);
        expected = "02-SEP-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 9);
        expected = "02-OKT-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 10);
        expected = "02-NOV-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

        c.set(Calendar.MONTH, 11);
        expected = "02-DEC-2021 @ 01:05";
        assertEquals(expected, Room.calendarToString(c));

    }
}