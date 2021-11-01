package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.entities.FeedbackType;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.mockserver.MockCommunication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.springtest.MockServerTest;
import org.mockserver.verify.VerificationTimes;
import org.springframework.test.context.junit4.SpringRunner;


@MockServerTest
@RunWith(SpringRunner.class)
public class ServerCommunicationTest {

    private static ClientAndServer mockServer;
    private static MockCommunication mockCommunication;

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    
    
    static Question q;
    static Room r;
    static Nickname n;
    static Poll p;
    static Feedback f;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(8080);
        mockCommunication = new MockCommunication(mockServer);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    /**
     * Sets up.
     */
    @BeforeEach
    public void setup() {
        q = new Question(1, "qUrl", "question", 4,
                false, null,42);

        r = new Room("rName", "rUrl", false, "rPwd", 1, 2);

        n = new Nickname(12, "nName", "nUrl", true, false);

        p = new Poll(1, "pUrl", "pTitle", "A", "B", "C", "D", null, null, true, false);

        f = new Feedback("fUrl", "fMsg", FeedbackType.FASTER, 12);
    }

    @Test
    void getQuestionsTestwhenUrlExist_Succes() throws Exception {
        List<Question> expected = new ArrayList<>();
        expected.add(q);

        mockCommunication.getAllQuestion(q.getRoomUrl(), false, false, expected);

        List<Question> actual = ServerCommunication
                .getQuestions(q.getRoomUrl(), false, false, q.getNicknameId());
        assertEquals(expected, actual);
    }


    @Test
    void createQuestionTest_Succes() throws Exception {
        mockCommunication.createQuestion(q.getRoomUrl(), q.getQuestion(), q.getId(), q);

        Question actual = ServerCommunication
                .createQuestion(q.getRoomUrl(), q.getQuestion(), q.getId());

        assertEquals(q, actual);
    }

    @Test
    void deleteQuestionTest_Succes() {
        HttpRequest request = mockCommunication.deleteQuestion(q.getId(), q.getNicknameId());

        ServerCommunication.deleteQuestion(q.getId(), q.getNicknameId());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void upvoteQuestionTest_Succes() {
        long nid = 12;

        HttpRequest request = mockCommunication.upvoteQuestion(q.getId(), nid);
        ServerCommunication.upvoteQuestion(q.getId(), nid);

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void markQuestionAsAnsweredTest_Succes() {
        long nid = 12;

        HttpRequest request = mockCommunication.questionMarkingAsAnswered(q.getId(), nid);
        ServerCommunication.markQuestionAsAnswered(q.getId(), nid);

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void createRoomTest_Succes() {
        Room req = new Room("name",  false, "pwd", 1, 2);

        mockCommunication.createRoom(req, r);

        Room actual = ServerCommunication.createRoom(req);
        assertEquals(r, actual);
    }

    @Test
    void answerQuestion() {
        String answer = "Answer";

        HttpRequest request = mockCommunication.answerQuestion(q.getId(), n.getId(), answer);
        ServerCommunication.answerQuestion(q.getId(), n.getId(), answer);

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void editRoom() {
        HttpRequest request = mockCommunication.editRoom(r, r.getModeratorPassword());
        ServerCommunication.editRoom(r, r.getModeratorPassword());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void getRooms() {
        List<Room> expected = new ArrayList<>();
        expected.add(r);
        expected.add(r);

        mockCommunication.getRooms(expected);

        List<Room> actual = ServerCommunication.getRooms();
        assertEquals(expected, actual);
    }

    @Test
    void getRoom() {
        mockCommunication.getRoom(r.getUrl(), r);

        Room actual = ServerCommunication.getRoom(r.getUrl());
        assertEquals(r, actual);
    }

    @Test
    void getNickname() {
        mockCommunication.getNickname(n.getId(), n);

        Nickname actual = ServerCommunication.getNickname(n.getId());
        assertEquals(n, actual);
    }

    @Test
    void getNickname2() {
        mockCommunication.getNickname(n.getRoomUrl(), n.getName(), n);

        Nickname actual = ServerCommunication.getNickname(n.getRoomUrl(), n.getName());
        assertEquals(n, actual);
    }

    @Test
    void createNicknameModerator() {
        mockCommunication
                .createNickname(n.getRoomUrl(), n.getName(), r.getModeratorPassword(), n);

        Nickname actual = ServerCommunication
                .createNickname(n.getRoomUrl(), n.getName(), r.getModeratorPassword());
        assertEquals(n, actual);
    }

    @Test
    void createNicknameNonModerator() {
        n = new Nickname(12, "nName", "nUrl", false, false);
        mockCommunication.createNickname(n.getRoomUrl(), n.getName(), "", n);

        Nickname actual = ServerCommunication.createNickname(n.getRoomUrl(), n.getName(), "");
        assertEquals(n, actual);
    }

    @Test
    void getPolls() {
        List<Poll> expected = new ArrayList<>();
        expected.add(p);

        mockCommunication.getPolls(p.getRoomUrl(), n.getId(), expected);

        List<Poll> actual = ServerCommunication.getPolls(p.getRoomUrl(), n.getId());
        p.setAnswered(true);
        assertEquals(expected, actual);
    }

    @Test
    void answerPoll() {
        int option = 3;
        mockCommunication.answerPoll(p.getId(), 3, n.getId());

        int actual = ServerCommunication.answerPoll(p.getId(), option, n.getId());
        assertEquals(option, actual);
    }

    @Test
    void getAmountOfParticipants() {
        int amount = 42;
        mockCommunication.getAmountOfPArticipants(r.getUrl(), amount);

        int actual = ServerCommunication.getAmountOfParticipants(r.getUrl());
        assertEquals(amount, actual);
    }

    @Test
    void getResults() {
        List<Integer> res = List.of(25, 50, 25, 0);
        mockCommunication.getResults(p.getId(), res);

        List<Integer> actual = ServerCommunication.getResults(p.getId());
        assertEquals(res, actual);
    }

    @Test
    void createPoll() {
        mockCommunication.createPoll(p);

        Poll actual = ServerCommunication.createPoll(p);
        assertEquals(p, actual);
    }

    @Test
    void deletePoll() {
        HttpRequest request = mockCommunication.deletePoll(p.getId(), n.getId());
        ServerCommunication.deletePoll(p.getId(), n.getId());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void openClosePoll() {
        HttpRequest request = mockCommunication.openClosePoll(p.getId(), n.getId(), true);
        ServerCommunication.openClosePoll(p.getId(), true, n.getId());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );

        HttpRequest request2 = mockCommunication.openClosePoll(p.getId(), n.getId(), false);
        ServerCommunication.openClosePoll(p.getId(), false, n.getId());

        mockServer.verify(
                request2,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void leaveRoom() {
        HttpRequest request = mockCommunication.leaveRoom(n.getId());
        ServerCommunication.leaveRoom(n.getId());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void createFeedback() {
        mockCommunication.createFeedback(f.getRoomUrl(), f.getMessage(),
                f.getFeedbackType(), f.getNicknameId(), f);

        Feedback actual = ServerCommunication.createFeedback(f.getRoomUrl(), f.getMessage(),
                f.getFeedbackType(), f.getNicknameId());
        assertEquals(f, actual);
    }

    @Test
    void getFeedbackByUrl() {
        List<Feedback> expected = List.of(f, f);
        mockCommunication.getFeedbackByUrl(f.getRoomUrl(), expected);

        List<Feedback> actual = ServerCommunication.getFeedbackByUrl(f.getRoomUrl());
        assertEquals(expected, actual);
    }

    @Test
    void changeNickname() {
        mockCommunication.changeNickname(n);

        Nickname actual = ServerCommunication.changeNickname(n);
        assertEquals(n, actual);
    }

    @Test
    void setCooldown() {
        int cd = 42;
        HttpRequest request = mockCommunication.setCooldown(r, cd, n.getId());
        ServerCommunication.setCooldown(r, cd, n.getId());

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

    @Test
    void muteNickname() {
        long moderatorNid = 21;
        HttpRequest request = mockCommunication.muteNickname(n.getId(), moderatorNid);
        ServerCommunication.muteNickname(n.getId(), moderatorNid);

        mockServer.verify(
                request,
                VerificationTimes.atLeast(1)
        );
    }

}
