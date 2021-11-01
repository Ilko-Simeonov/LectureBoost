package nl.tudelft.oopp.demo.mockserver;

import static org.mockserver.model.Cookie.cookie;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.StringBody.exact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.client.Entity;

import java.io.IOException;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.entities.FeedbackType;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import org.junit.jupiter.api.Test;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;

public class MockCommunication {

    private static String sessionId = UUID.randomUUID().toString();

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd").serializeNulls().create();
    
    private ClientAndServer mockServer;

    public MockCommunication(ClientAndServer mockServer) {
        this.mockServer = mockServer;
    }

    /**
     * Simulate endpoint for create question.
     *
     * @param roomUrl  the room url
     * @param question the question
     * @param nid      the nid
     * @param response the response
     */
    public void createQuestion(String roomUrl, String question, long nid, Question response) {
        Question req = new Question(question, nid);

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/question/" + roomUrl + "/create")
                                .withBody(gson.toJson(req))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(response)));
    }

    /**
     * Simulate endpoint for Gets all question.
     *
     * @param roomUrl  the room url
     * @param ordered  the ordered
     * @param answered the answered
     * @param response the response
     */
    public void getAllQuestion(String roomUrl, boolean ordered,
                               boolean answered, List<Question> response) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/question/" + roomUrl + "/get")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(response)));
    }

    /**
     * Simulate endpoint for Delete question http request.
     *
     * @param id  the id
     * @param nid the nid
     * @return the http request
     */
    public HttpRequest deleteQuestion(long id, long nid) {
        HttpRequest request = request()
                                .withMethod("DELETE")
                                .withPath("/question/" + id + "/delete")
                                .withQueryStringParameter("n", Long.toString(nid));
        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Upvote question http request.
     *
     * @param id  the id
     * @param nid the nid
     * @return the http request
     */
    public HttpRequest upvoteQuestion(long id, long nid) {
        HttpRequest request = request()
                                .withMethod("GET")
                                .withPath("/question/upvote/" + id)
                                .withQueryStringParameter("n", Long.toString(nid));
        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Question marking as answered http request.
     *
     * @param qid the qid
     * @param nid the nid
     * @return the http request
     */
    public HttpRequest questionMarkingAsAnswered(long qid, long nid) {
        HttpRequest request = request()
                                .withMethod("GET")
                                .withPath("/question/" + qid + "/markanswered")
                                .withQueryStringParameter("n", Long.toString(nid));

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Answer question http request.
     *
     * @param qid    the qid
     * @param nid    the nid
     * @param answer the answer
     * @return the http request
     */
    public HttpRequest answerQuestion(long qid, long nid, String answer) {
        HttpRequest request = request()
                .withMethod("PUT")
                .withPath("/question/" + qid + "/" + nid + "/answer")
                .withBody(answer);
        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Create room.
     *
     * @param req the req
     * @param res the res
     */
    public void createRoom(Room req, Room res) {
        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/room/create")
                                .withBody(gson.toJson(req))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Edit room http request.
     *
     * @param r           the r
     * @param modPassword the mod password
     * @return the http request
     */
    public HttpRequest editRoom(Room r, String modPassword) {
        HttpRequest request = request()
                                .withMethod("PUT")
                                .withPath("/room/edit")
                                .withBody(gson.toJson(r))
                                .withQueryStringParameter("p", modPassword);
        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Gets rooms.
     *
     * @param res the res
     */
    public void getRooms(List<Room> res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/room/get")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Gets room.
     *
     * @param roomUrl the room url
     * @param res     the res
     */
    public void getRoom(String roomUrl, Room res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/room/" + roomUrl + "/scheduled")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Gets nickname.
     *
     * @param id  the id
     * @param res the res
     */
    public void getNickname(long id, Nickname res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/nickname/get/" + id)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Gets nickname.
     *
     * @param roomUrl the room url
     * @param name    the name
     * @param res     the res
     */
    public void getNickname(String roomUrl, String name, Nickname res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/nickname/" + roomUrl + "/get/" + name)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Create nickname.
     *
     * @param roomUrl     the room url
     * @param name        the name
     * @param modPassword the mod password
     * @param response    the response
     */
    public void createNickname(String roomUrl, String name, String modPassword, Nickname response) {

        Nickname req = new Nickname(name, roomUrl);

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/nickname/create")
                                .withBody(gson.toJson(req))
                                .withQueryStringParameter("p", modPassword)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(response)));
    }

    /**
     * Simulate endpoint for Gets polls.
     *
     * @param roomUrl the room url
     * @param nid     the nid
     * @param res     the res
     */
    public void getPolls(String roomUrl, long nid, List<Poll> res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/polls/" + roomUrl + "/get/ordered")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));

        for (Poll p : res) {
            mockServer
                    .when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/polls/" + p.getId() + "/hasAnswered")
                                    .withQueryStringParameter("n", Long.toString(nid))
                    )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(gson.toJson(true)));
        }
    }

    /**
     * Simulate endpoint for Answer poll.
     *
     * @param pid    the pid
     * @param option the option
     * @param nid    the nid
     */
    public void answerPoll(long pid, int option, long nid) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/polls/" + pid + "/answer/" + option)
                                .withQueryStringParameter("n", Long.toString(nid))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(option)));
    }

    /**
     * Simulate endpoint for Gets amount of p articipants.
     *
     * @param roomUrl the room url
     * @param res     the res
     */
    public void getAmountOfPArticipants(String roomUrl, int res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/nickname/" + roomUrl + "/participants")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Gets results.
     *
     * @param pid the pid
     * @param res the res
     */
    public void getResults(long pid, List<Integer> res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/polls/" + pid + "/getresults")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Create poll.
     *
     * @param p the p
     */
    public void createPoll(Poll p) {
        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/polls/create")
                                .withBody(gson.toJson(p))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(p)));
    }

    /**
     * Simulate endpoint for Delete poll http request.
     *
     * @param pid the pid
     * @param nid the nid
     * @return the http request
     */
    public HttpRequest deletePoll(long pid, long nid) {
        HttpRequest request = request()
                .withMethod("DELETE")
                .withPath("/polls/" + pid + "/delete")
                .withQueryStringParameter("n", Long.toString(nid));

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Open close poll http request.
     *
     * @param pid  the pid
     * @param nid  the nid
     * @param open the open
     * @return the http request
     */
    public HttpRequest openClosePoll(long pid, long nid, boolean open) {
        String path = "/polls/" + pid;
        if (open) {
            path += "/open";
        } else {
            path += "/close";
        }
        HttpRequest request = request()
                .withMethod("GET")
                .withPath(path)
                .withQueryStringParameter("n", Long.toString(nid));

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Leave room http request.
     *
     * @param nid the nid
     * @return the http request
     */
    public HttpRequest leaveRoom(long nid) {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/nickname/leave/" + nid);

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Create feedback.
     *
     * @param roomUrl      the room url
     * @param message      the message
     * @param feedbackType the feedback type
     * @param nid          the nid
     * @param res          the res
     */
    public void createFeedback(String roomUrl, String message,
                                      FeedbackType feedbackType, long nid, Feedback res) {

        Feedback req = new Feedback(roomUrl, message, feedbackType, nid);

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/feedback/create")
                                .withBody(gson.toJson(req))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Gets feedback by url.
     *
     * @param roomUrl the room url
     * @param res     the res
     */
    public void getFeedbackByUrl(String roomUrl, List<Feedback> res) {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/feedback/" + roomUrl + "/get")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(res)));
    }

    /**
     * Simulate endpoint for Change nickname.
     *
     * @param n the n
     */
    public void changeNickname(Nickname n) {
        mockServer
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/nickname/change")
                                .withBody(gson.toJson(n))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(n)));
    }

    /**
     * Simulate endpoint for Sets cooldown.
     *
     * @param room     the room
     * @param cooldown the cooldown
     * @param nid      the nid
     * @return the cooldown
     */
    public HttpRequest setCooldown(Room room, int cooldown, long nid) {
        room.setQuestionCooldown(cooldown);

        HttpRequest request = request()
                                .withMethod("PUT")
                                .withPath("/room/set_cooldown")
                                .withBody(gson.toJson(room))
                                .withQueryStringParameter("n", Long.toString(nid));

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }

    /**
     * Simulate endpoint for Mute nickname http request.
     *
     * @param nid the nid
     * @param n   the n
     * @return the http request
     */
    public HttpRequest muteNickname(long nid, long n) {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/nickname/" + nid + "/mute")
                .withQueryStringParameter("n", Long.toString(n));

        mockServer
                .when(
                        request
                )
                .respond(
                        response()
                                .withStatusCode(200));

        return request;
    }
}


