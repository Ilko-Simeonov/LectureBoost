package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;



class RoomTest {

    @Test
    void roomConstructorTest1() {
        Room r = new Room(null, null, false,
                null, 1, 2);
        assertNotNull(r);
    }

    @Test
    void roomConstructorTest2() {

        Room r = new Room("null", false, "Test", 1, 2);
        assertNotNull(r);
    }

    @Test
    void roomConstructorTest3() {
        Room r = new Room("test", "test", true, "Pass", 1, 2);
        assertNotNull(r);
    }

    @Test
    void testToString1_without_id_mod() {
        Room r = new Room("test", "test", true, "Pass", 1, 2);
        String expected = "Room{"
                + "name='" + r.getName() + '\''
                + ", url='" + r.getUrl() + '\''
                + ", moderatorPassword='" + r.getModeratorPassword() + '\''
                + ", open=" + r.isOpen()
                + ", starttime='" + r.getStartTime() + '\''
                + ", endtime='" + r.getEndTime() + '\''
                + ", questionCooldown=" + r.getQuestionCooldown()
                + '}';
        String toBeTested = r.toString();
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void testToString1_all_value() {
        Room r = new Room("null", false, "Pass", 1, 2);
        String expected = "Room{"
                + "name='" + r.getName() + '\''
                + ", url='" + r.getUrl() + '\''
                + ", moderatorPassword='" + r.getModeratorPassword() + '\''
                + ", open=" + r.isOpen()
                + ", starttime='" + r.getStartTime() + '\''
                + ", endtime='" + r.getEndTime() + '\''
                + ", questionCooldown=" + r.getQuestionCooldown()
                + '}';
        String toBeTested = r.toString();
        assertEquals(expected, toBeTested,
                "The strings are not the same...");
    }

    @Test
    void testToString2_without_id_mod() {
        Room r = new Room("notTest", "notTest", true, "Pass", 1, 2);
        String expected = "Room{"
                + "name='" + r.getName() + '\''
                + ", url='" + r.getUrl() + '\''
                + ", moderatorPassword='" + r.getModeratorPassword() + '\''
                + ", open=" + r.isOpen()
                + ", starttime='" + r.getStartTime() + '\''
                + ", endtime='" + r.getEndTime() + '\''
                + ", questionCooldown=" + r.getQuestionCooldown()
                + '}';
        String toBeTested = r.toString();
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void testToString2_all_value() {
        Room r = new Room("notTest", "notTest", false, "Pass", 1, 2);
        String expected = "Room{"
                + "name='" + r.getName() + '\''
                + ", url='" + r.getUrl() + '\''
                + ", moderatorPassword='" + r.getModeratorPassword() + '\''
                + ", open=" + r.isOpen()
                + ", starttime='" + r.getStartTime() + '\''
                + ", endtime='" + r.getEndTime() + '\''
                + ", questionCooldown=" + r.getQuestionCooldown()
                + '}';
        String toBeTested = r.toString();
        assertEquals(expected, toBeTested,
                "The strings are not the same...");
    }

    @Test
    void equals_same_room() {
        Room testRoom = new Room("notTest", "notTest", true, "true", 1, 2);
        assertEquals(testRoom, testRoom,
                "The room should be the same room as itself...");
    }

    @Test
    void equals_other_room_false1() {
        Room testRoom = new Room("notTest", "notTest", true, "true", 2, 3);
        Room anotherRoom = new Room("Test", "Test", true, "true", 3, 4);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same...");
    }

    @Test
    void equals_other_room_false_different_isOpenBoolean() {
        Room testRoom = new Room("notTest", "notTest", true, "Test", 1, 2);
        Room anotherRoom = new Room("notTest", "notTest", false, "Test", 1, 2);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since one is open and the other is not...");
    }

    @Test
    void equals_other_room_false_different_url() {
        Room testRoom = new Room("notTest", "notTest", false, "true", 1, 2);
        Room anotherRoom = new Room("notTest", "Test", false, "true", 1, 2);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the urls are different...");
    }

    @Test
    void equals_other_room_false_different_url2() {

        Room testRoom = new Room("notTest", "notTest", false, "true", 1, 2);
        Room anotherRoom = new Room("notTest", "123", false, "true", 1, 2);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the urls are different...");
    }

    @Test
    void equals_other_room_false_different_name() {
        Room testRoom = new Room("notTest", "notTest", true, "true", 1, 2);
        Room anotherRoom = new Room("Test", "notTest", true, "true", 1, 2);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the names are not the same...");
    }

    @Test
    void equals_other_room_false_different_name2() {

        Room testRoom = new Room("notTest", "notTest", true,"true", 1, 2);
        Room anotherRoom = new Room("123", "notTest", true,"true", 1, 2);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the names are not the same...");
    }

    @Test
    void equals_other_room_false_different_startTime() {
        Room testRoom = new Room("notTest", "notTest", true, "true", 1, 3);
        Room anotherRoom = new Room("notTest", "notTest", true, "true", 2, 3);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the startTimes are not the same...");
    }

    @Test
    void equals_other_room_false_different_endTime() {
        Room testRoom = new Room("notTest", "notTest", true,"true", 1, 2);
        Room anotherRoom = new Room("notTest", "notTest", true,"true", 1, 3);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since the dates are not the same...");
    }


    @Test
    void equals_other_room_false_different_allValues() {
        Room testRoom = new Room("notTest", true, "notTest", 1, 3);
        Room anotherRoom = new Room("Test", false, "Test", 2, 4);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since all values are different...");
    }

    @Test
    void equals_other_room_false_only_same_openBoolean() {
        Room testRoom = new Room("notTest", "notTest", true, "false", 1, 3);
        Room anotherRoom = new Room("Test", "123", true, "1", 2, 4);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same "
                        + "since both rooms are open but differ on all other aspects...");
    }

    @Test
    void equals_other_room_false_only_same_url() {
        Room testRoom = new Room("notTest", "notTest", true, "false", 2, 4);
        Room anotherRoom = new Room("Test", "notTest", false, "false1", 5, 7);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since only the url are the same "
                + "(however this should never be the case as urls are unique)...");
    }

    @Test
    void equals_other_room_false_only_same_url2() {
        Room testRoom = new Room("notTest", "123", false, "false", 1, 2);
        Room anotherRoom = new Room("Test", "123", false, "falseq", 3, 4);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since only the url are the same "
                        + "(however this should never be the case as urls are unique)...");
    }

    @Test
    void equals_other_room_false_only_same_name() {
        Room testRoom = new Room("notTest", "notTest", true, "false", 1, 2);
        Room anotherRoom = new Room("notTest", "123", false, "false1", 2, 3);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since only the names are the same...");
    }

    @Test
    void equals_other_room_false_only_same_name2() {
        Room testRoom = new Room("123", "notTest", false, "false", 1, 2);
        Room anotherRoom = new Room("123", "123", true, "false1", 2, 3);
        assertNotEquals(testRoom, anotherRoom,
                "The two rooms should not be the same since only the names are the same...");
    }

    @Test
    void equals_other_room_true1() {
        Room testRoom = new Room("notTest", "notTest", false, "false", 1, 2);
        Room anotherRoom = new Room("notTest", "notTest", false, "false", 1, 2);
        assertEquals(testRoom, anotherRoom,
                "The two rooms should be the same since all value are the same...");
    }

    @Test
    void equals_other_room_true2() {
        Room testRoom = new Room("123", "123", false, "true",
                1, 2);
        Room anotherRoom = new Room("123", "123", false, "true",
                1, 2);
        assertEquals(testRoom, anotherRoom,
                "The two rooms should be the same since all values are the same...");
    }

    @Test
    void room_question_not_equals_test() {
        Room testRoom = new Room("notTest", "notTest", false, "false", 1, 2);
        Question question = new Question();
        assertNotEquals(testRoom, question, "A room is not a question...");
    }

    @Test
    void getter_Name_test() {
        Room testRoom = new Room("notTest", "notTest", false, "false",  12,  13);
        String testName = testRoom.getName();
        assertNotNull(testName);
    }

    @Test
    void getter_Url_test() {
        Room testRoom = new Room("notTest", "notTest", false,"false", 1, 1);
        String testRoomUrl = testRoom.getUrl();
        assertNotNull(testRoomUrl);
    }

    @Test
    void getter_ModeratorPass_test() {
        Room testRoom = new Room("notTest", "notTest", false, "false", 1, 1);
        String testPassword = testRoom.getModeratorPassword();
        assertNotNull(testPassword);
    }

    @Test
    void getter_IsOpen_test() {
        Room testRoom = new Room("notTest", "notTest", false, "false", 1, 1);
        boolean testRoomOpen = testRoom.isOpen();
        assertNotNull(testRoomOpen);
    }
}