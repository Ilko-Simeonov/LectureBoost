package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void questionConstructorTest1() {
        Question q = new Question(null, 1);
        assertNotNull(q);
    }

    @Test
    void questionConstructorTest2() {
        Question q = new Question("Is this a test question?", 1);
        assertNotNull(q);
    }

    @Test
    void questionConstructorTest3() {
        Question q = new Question(100, "testUrl",
                "Will this be on the test?", 1, true, "Yes",1);
        assertNotNull(q);
    }

    @Test
    void testToString1() {
        Question question = new Question("How much flour do you need for a strawberry shortcake?",
                1);
        String toBeTested = question.toString();
        String expected = "Question{"
                + "id=" + question.getId()
                + ", roomUrl='" + question.getRoomUrl() + '\''
                + ", question='" + question.getQuestion() + '\''
                + ", upvotes=" + question.getUpvotes() + '\''
                + ", answered=" + question.isAnswered() + '\''
                + ", answer=" + question.getAnswer() + '\''
                + ", nicknameId=" + question.getNicknameId()
                + ", upvoted=" + question.isUpvoted()
                + '}';
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void testToString2() {
        Question question = new Question("Is this a rhetoric question?", 2);
        String toBeTested = question.toString();
        String expected = "Question{"
                + "id=" + question.getId()
                + ", roomUrl='" + question.getRoomUrl() + '\''
                + ", question='" + question.getQuestion() + '\''
                + ", upvotes=" + question.getUpvotes() + '\''
                + ", answered=" + question.isAnswered() + '\''
                + ", answer=" + question.getAnswer() + '\''
                + ", nicknameId=" + question.getNicknameId()
                + ", upvoted=" + question.isUpvoted()
                + '}';
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void testToString3() {
        Question question = new Question(null, 3);
        String toBeTested = question.toString();
        String expected = "Question{"
                + "id=" + question.getId()
                + ", roomUrl='" + question.getRoomUrl() + '\''
                + ", question='" + question.getQuestion() + '\''
                + ", upvotes=" + question.getUpvotes() + '\''
                + ", answered=" + question.isAnswered() + '\''
                + ", answer=" + question.getAnswer() + '\''
                + ", nicknameId=" + question.getNicknameId()
                + ", upvoted=" + question.isUpvoted()
                + '}';
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void testToString4() {
        Question question = new Question("null", 4);
        String toBeTested = question.toString();
        String expected = "Question{"
                + "id=" + question.getId()
                + ", roomUrl='" + question.getRoomUrl() + '\''
                + ", question='" + question.getQuestion() + '\''
                + ", upvotes=" + question.getUpvotes() + '\''
                + ", answered=" + question.isAnswered() + '\''
                + ", answer=" + question.getAnswer() + '\''
                + ", nicknameId=" + question.getNicknameId()
                + ", upvoted=" + question.isUpvoted()
                + '}';
        assertEquals(expected, toBeTested, "The strings are not the same...");
    }

    @Test
    void getter_Id_test() {
        Question question = new Question(1, "null", "What?", 4,
                false, "Answer", 42);
        long idTest = question.getId();
        assertNotNull(idTest);
    }

    @Test
    void getter_roomUrl_test() {
        Question question = new Question(1, "null", "What?", 4,
                false, "Answer", 42);
        String urlTest = question.getRoomUrl();
        assertNotNull(urlTest);
    }

    @Test
    void getter_question_test() {
        Question question = new Question(1, "null", "What?", 4,
                false, "Answer", 42);
        String questionTest = question.getQuestion();
        assertNotNull(questionTest);
    }

    @Test
    void getter_Upvotes_test() {
        Question question = new Question(1, "null", "What?", 4,
                false, "Answer", 42);
        long upvotesTest = question.getUpvotes();
        assertNotNull(upvotesTest);
    }

    @Test
    void getter_isAnswered_test() {
        Question question = new Question(1, "null", "What?", 4,
                false, "Answer", 42);
        boolean answerTest = question.isAnswered();
        assertNotNull(answerTest);
    }

    @Test
    void getter_NicknameId_test() {
        Question question = new Question(1, "null", "What?",
                4, false, "Answer",42);
        long nickIdTest = question.getNicknameId();
        assertNotNull(nickIdTest);
    }
}