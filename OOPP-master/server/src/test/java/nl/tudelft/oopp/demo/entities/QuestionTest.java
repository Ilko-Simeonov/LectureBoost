package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionTest {

    Question question1;
    Question question2;
    Question question3;
    Question question4;

    @BeforeEach
    void setUp() {
        question1 = new Question("what");
        question2 = new Question("asdf", "url");
        question3 = new Question(99L, "url2", "abc", 500, 0, false);
        question4 = new Question(98L, "url3", "how", 333, 9, true, "yes");
    }

    @Test
    void emptyConstructor() {
        assertNotNull(new Question());
    }

    @Test
    void getId() {
        assertEquals(99L, question3.getId());
        assertEquals(98L, question4.getId());
    }

    @Test
    void getQuestion() {
        assertEquals("what", question1.getQuestion());
        assertEquals("asdf", question2.getQuestion());
        assertEquals("abc", question3.getQuestion());
        assertEquals("how", question4.getQuestion());
    }

    @Test
    void getUpvotes() {
        assertEquals(0,question1.getUpvotes());
        assertEquals(0,question2.getUpvotes());
        assertEquals(500,question3.getUpvotes());
        assertEquals(333,question4.getUpvotes());
    }

    @Test
    void getRoomUrl() {
        assertNull(question1.getRoomUrl());
        assertEquals("url", question2.getRoomUrl());
        assertEquals("url2", question3.getRoomUrl());
        assertEquals("url3", question4.getRoomUrl());
    }

    @Test
    void isAnswered() {
        assertFalse(question1.isAnswered());
        assertFalse(question2.isAnswered());
        assertFalse(question3.isAnswered());
        assertTrue(question4.isAnswered());
    }

    @Test
    void getAnswer() {
        assertNull(question1.getAnswer());
        assertNull(question2.getAnswer());
        assertNull(question3.getAnswer());
        assertEquals("yes", question4.getAnswer());
    }

    @Test
    void getCsvData() {
        List<String> list = question1.getCsvData();
        assertEquals("0", list.get(0));
        assertNull(list.get(1));
        assertEquals("what", list.get(2));
        assertEquals("0", list.get(3));
        assertEquals("false", list.get(4));
    }
    
    @Test
    void setUrl() {
        question1.setUrl("differentUrl1");
        question2.setUrl("differentUrl2");
        question3.setUrl("differentUrl3");
        question4.setUrl("differentUrl4");
        
        assertEquals("differentUrl1", question1.getRoomUrl());
        assertEquals("differentUrl2", question2.getRoomUrl());
        assertEquals("differentUrl3", question3.getRoomUrl());
        assertEquals("differentUrl4", question4.getRoomUrl());
    }

    @Test
    void setUpvotes() {
        question1.setUpvotes(1);
        question2.setUpvotes(2);
        question3.setUpvotes(3);
        question4.setUpvotes(4);

        assertEquals(1, question1.getUpvotes());
        assertEquals(2, question2.getUpvotes());
        assertEquals(3, question3.getUpvotes());
        assertEquals(4, question4.getUpvotes());
    }
    
    @Test
    void setAnswer() {
        question1.setAnswer("Answer 1");
        question2.setAnswer("Answer 2");
        question3.setAnswer("Answer 3");
        question4.setAnswer("Answer 4");
        
        
        assertEquals("Answer 1", question1.getAnswer());
        assertEquals("Answer 2", question2.getAnswer());
        assertEquals("Answer 3", question3.getAnswer());
        assertEquals("Answer 4", question4.getAnswer());
    }
    
    @Test
    void setAnswered() {
        question1.setAnswered(true);
        assertTrue(question1.isAnswered());
        
        question1.setAnswered(false);
        assertFalse(question1.isAnswered());

        question4.setAnswered(false);
        assertFalse(question4.isAnswered());

        question4.setAnswered(true);
        assertTrue(question4.isAnswered());
    }
    
    @Test
    void setNicknameId() {
        question1.setNicknameId(1);
        question2.setNicknameId(2);
        question3.setNicknameId(3);
        question4.setNicknameId(4);

        assertEquals(1, question1.getNicknameId());
        assertEquals(2, question2.getNicknameId());
        assertEquals(3, question3.getNicknameId());
        assertEquals(4, question4.getNicknameId());
    }

    @Test
    void testToString() {
        String string = question1.toString();
        String test = "Question{id=0, roomUrl='null', question='what',";
        test += " nicknameId='0', answered='false', answer='null', answeredById='0', upvotes=0}";
        assertEquals(test, string);
    }
    
    @Test
    void notEquals() {
        assertNotEquals(question1, question2);
        assertNotEquals(question1, question3);
        assertNotEquals(question1, question4);
        assertNotEquals(question2, question3);
        assertNotEquals(question2, question4);
        assertNotEquals(question3, question4);

        assertEquals(new Question("a", "b"), new Question("a", "b"));
        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url2", "question", 22, 4, false, "answer"));

        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url", "question2", 22, 4, false, "answer"));

        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url", "question", 23, 4, false, "answer"));

        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url", "question", 22, 5, false, "answer"));

        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url", "question", 22, 4, true, "answer"));

        assertNotEquals(
                new Question(0, "url", "question", 22, 4, false, "answer"),
                new Question(0, "url", "question", 22, 4, false, "answer2"));

    }

    @Test
    void equalsNull() {
        assertFalse(question1.equals(null));
    }

    @Test
    void equalsDifferentClass() {
        assertFalse(question1.equals(new Object()));
    }

    @Test
    void equalsSelf() {
        assertEquals(question1, question1);
        assertEquals(question2, question2);
        assertEquals(question3, question3);
        assertEquals(question4, question4);
    }
}