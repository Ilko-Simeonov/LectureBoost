package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.entities.FeedbackType;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServerCommunication {

    private static final HttpClient client = HttpClient.newBuilder().build();

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    /**
     * Gets questions for a certain roomUrl.
     * If roomUrl equals "", it gets all questions for all roomUrls
     * If ordered is true, then it returns the questions ordered by upvotes (descending)
     * If answered is true, only gets answered questions, if it's false, only unanswered questions.
     *
     * @param roomUrl the room url
     * @param ordered whether the questions should be ordered by upvotes (descending)
     * @return the questions for that url, possibly ordered by upvotes (descending)
     */
    public static List<Question> getQuestions(String roomUrl, boolean ordered,
                                              boolean answered, long nid) {
        String url = "http://localhost:8080/question/";

        if (!roomUrl.equals("")) {
            url += roomUrl + "/";
        }

        url += "get";

        if (ordered) {
            url += "/ordered";
        } else if (answered) {
            //There is no endpoint (yet?) that returns answered questions ordered by anything.
            url += "/answered";
        }

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Type type = new TypeToken<List<Question>>(){}.getType();

            List<Question> questions = gson.fromJson(response, type);

            for (Question q : questions) {

                String url2 = "http://localhost:8080/question/" + q.getId() + "/hasupvoted?n=" + nid;

                String response2 = ClientBuilder.newClient()
                        .target(url2)
                        .request()
                        .get(String.class);

                boolean upvoted = Boolean.parseBoolean(response2);
                q.setUpvoted(upvoted);
            }

            return questions;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * To download CSV.
     * @param roomUrl url of the room
     * @param stage stage of the process
     */
    public static void downloadAnsweredQuestionsAsCsv(String roomUrl, Stage stage) {
        String url = "http://localhost:8080/question/";
        if (roomUrl != null && !roomUrl.equals("")) {
            url += roomUrl + "/";
        }
        url += "get/answered_csv";
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.setInitialFileName("answered_questions"
                    + (roomUrl == null ? "" : "-"
                            + roomUrl) + ".csv");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                    "All Files",
                    "*.*"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                URL website = new URL(url);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(file);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                JOptionPane.showMessageDialog(null, "File has been downloaded and saved.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create question.
     *  @param roomUrl  the room url
     * @param question the question
     */
    public static Question createQuestion(String roomUrl, String question, long nid) {
        Question q = new Question(question, nid);

        Entity<Question> requestBody = Entity.entity(q, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/" + roomUrl + "/create")
                    .request()
                    .post(requestBody, String.class);
            return gson.fromJson(response, Question.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete question with a certain id.
     *
     * @param id the id
     */
    public static void deleteQuestion(long id, long nid) {
        try {
            Response response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/" + id + "/delete?n=" + nid)
                    .request()
                    .delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Upvote a question by its id.
     *
     * @param qid the question_id
     * @param nid the nickname_id
     */
    public static void upvoteQuestion(long qid, long nid) {
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/upvote/" + qid + "?n=" + nid)
                    .request()
                    .get(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mark question as answered by id.
     *
     * @param qid the question id
     * @param nid the nickname id
     */
    public static void markQuestionAsAnswered(long qid, long nid) {
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/" + qid + "/markanswered?n=" + nid)
                    .request()
                    .get(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Answer question by id.
     *
     * @param qid the question id
     * @param nid the nickname id
     * @param answer the answer response
     */
    public static void answerQuestion(long qid, long nid, String answer) {
        Entity<String> requestBody = Entity.entity(answer, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/" + qid + "/" + nid + "/answer")
                    .request()
                    .put(requestBody, String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Edit question by id.
     *
     * @param qid the question id
     * @param nid the nickname id
     * @param change the question
     */
    public static void editQuestion(long qid, long nid, String change) {
        Entity<String> requestBody = Entity.entity(change, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/question/" + qid + "/" + nid + "/edit")
                    .request()
                    .put(requestBody, String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create room.
     *
     * @param r the room
     * @return the room created on the server (with id and url set)
     */
    public static Room createRoom(Room r) {
        Entity<Room> requestBody = Entity.entity(r, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/room/create")
                    .request()
                    .post(requestBody, String.class);
            Room room = gson.fromJson(response, Room.class);
            return room;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Edits a room.
     * @param r - the updated room that will replace the old one.
     */
    public static void editRoom(Room r, String modPassword) {
        Entity<Room> requestBody = Entity.entity(r, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/room/edit?p=" + modPassword)
                    .request()
                    .put(requestBody, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all rooms.
     *
     * @return the rooms
     */
    public static List<Room> getRooms() {
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/room/get")
                    .request()
                    .get(String.class);

            Type type = new TypeToken<List<Room>>(){}.getType();

            List<Room> rooms = gson.fromJson(response, type);

            return rooms;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Gets room by url.
     *
     * @param roomUrl the room url
     * @return the room
     */
    public static Room getRoom(String roomUrl) {
        String url = "http://localhost:8080/room/" + roomUrl + "/scheduled";
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Room room = gson.fromJson(response, Room.class);
            return room;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete a room with a certain id.
     *
     * @param id the id
     */
    public static void deleteRoom(long id) {
        try {
            Response response = ClientBuilder.newClient()
                    .target("http://localhost:8080/room/delete/" + id)
                    .request()
                    .delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open or close a room with a certain id.
     *
     * @param id   the id
     * @param open - if true it opens the room, else it closes the room
     */
    public static void openRoom(long id, boolean open) {
        String url = "http://localhost:8080/room/";

        if (open) {
            url += "open/";
        } else {
            url += "close/";
        }

        url += id;

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets nickname bt a certain id.
     *
     * @param id the id
     * @return the nickname
     */
    public static Nickname getNickname(long id) {
        String url = "http://localhost:8080/nickname/get/" + id;

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Nickname nickname = gson.fromJson(response, Nickname.class);
            return nickname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a nickname.
     *
     * @param room the room_url
     * @param name the name
     * @return the nickname
     */
    public static Nickname getNickname(String room, String name) {
        String url = "http://localhost:8080/nickname/";
        url += room + "/get/" + name;

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Nickname nickname = gson.fromJson(response, Nickname.class);
            return nickname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create nickname.
     *
     * @param room the room_url
     * @param name the name
     * @return the nickname (with id)
     */
    public static Nickname createNickname(String room, String name, String modPassword) {
        Nickname n = new Nickname(name, room);

        Entity<Nickname> requestBody = Entity.entity(n, MediaType.APPLICATION_JSON);
        String response = ClientBuilder.newClient()
                .target("http://localhost:8080/nickname/create?p=" + modPassword)
                .request()
                .post(requestBody, String.class);
        Nickname nickname = gson.fromJson(response, Nickname.class);
        return nickname;
    }

    /**
     * Gets all polls (ordered -> active polls first).
     *
     * @param room the roomurl
     * @param nid  the nickname_id
     * @return the polls
     */
    public static List<Poll> getPolls(String room, long nid) {
        String url = "http://localhost:8080/polls/" + room + "/get/ordered";
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Type type = new TypeToken<List<Poll>>(){}.getType();

            List<Poll> polls = gson.fromJson(response, type);

            for (Poll p : polls) {
                String url2 = "http://localhost:8080/polls/" + p.getId() + "/hasanswered?n=" + nid;

                String response2 = ClientBuilder.newClient()
                        .target(url2)
                        .request()
                        .get(String.class);

                boolean answered = Boolean.parseBoolean(response2);
                p.setAnswered(answered);
            }

            return polls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Answers poll.
     *
     * @param pollId     the poll id
     * @param option     the chosen option
     * @param nicknameId the nickname id
     * @return the chosen option iff successful, otherwise returns -1
     */
    public static int answerPoll(long pollId, int option, long nicknameId) {
        String url = "http://localhost:8080/polls/";
        url += pollId + "/answer/" + option + "?n=" + nicknameId;
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);
            int answered = gson.fromJson(response, Integer.class);
            return answered;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets amount of participants in a room.
     *
     * @param roomUrl the room url
     * @return the amount of participants
     */
    public static int getAmountOfParticipants(String roomUrl) {
        String url = "http://localhost:8080/nickname/" + roomUrl + "/participants";
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);
            int participants = gson.fromJson(response, Integer.class);
            return participants;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Returns list per option of how many percent of people answered with that option.
     *
     * @param pollId the poll_id
     * @return the percentages
     */
    public static List<Integer> getResults(long pollId) {
        String url = "http://localhost:8080/polls/" + pollId + "/getresults";
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);

            Type type = new TypeToken<List<Integer>>(){}.getType();

            List<Integer> results = gson.fromJson(response, type);

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create new poll.
     *
     * @param p the poll
     * @return the poll
     */
    public static Poll createPoll(Poll p) {
        Entity<Poll> requestBody = Entity.entity(p, MediaType.APPLICATION_JSON);
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/polls/create")
                    .request()
                    .post(requestBody, String.class);
            Poll poll = gson.fromJson(response, Poll.class);
            return poll;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete poll.
     *
     * @param pid the poll_id
     * @param nid the nickname_id
     */
    public static void deletePoll(long pid, long nid) {
        try {
            Response response = ClientBuilder.newClient()
                    .target("http://localhost:8080/polls/" + pid + "/delete?n=" + nid)
                    .request()
                    .delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open or close a poll poll.
     *
     * @param pid  the poll_id
     * @param open whether the poll should be set to open or closed
     * @param nid  the nickname_id
     */
    public static void openClosePoll(long pid, boolean open, long nid) {
        String url = "http://localhost:8080/polls/";

        url += pid;

        if (open) {
            url += "/open";
        } else {
            url += "/close";
        }

        url += "?n=" + nid;

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets nickname as inactive on the server side when leaving a room.
     *
     * @param nid the nickname_id
     */
    public static void leaveRoom(long nid) {
        String url = "http://localhost:8080/nickname/leave/" + nid;
        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates feedback given attributes.
     * @param room room url
     * @param message message of feedback
     * @param feedbackType feedbackType
     * @return
     */
    public static Feedback createFeedback(String room, String message,
                                          FeedbackType feedbackType, long nickNameId) {
        Feedback fb = new Feedback(room, message, feedbackType, nickNameId);
        Entity<Feedback> requestBody = Entity.entity(fb, MediaType.APPLICATION_JSON);
        String response = ClientBuilder.newClient()
                .target("http://localhost:8080/feedback/create")
                .request()
                .post(requestBody, String.class);
        Feedback feedback = gson.fromJson(response, Feedback.class);
        return feedback;
    }

    /**
     * Returns feedback in a specified room.
     * @param roomUrl url of room
     * @return feedback for specified url
     */
    public static List<Feedback> getFeedbackByUrl(String roomUrl) {
        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/feedback/" + roomUrl + "/get")
                    .request()
                    .get(String.class);

            Type type = new TypeToken<List<Feedback>>(){}.getType();

            List<Feedback> feedback = gson.fromJson(response, type);

            return feedback;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Changes the nickname.
     * @param n - the updated Nickname.
     * @return the Nickname
     */
    public static Nickname changeNickname(Nickname n) {
        Entity<Nickname> requestBody = Entity.entity(n, MediaType.APPLICATION_JSON);

        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/nickname/change")
                    .request()
                    .put(requestBody, String.class);

            Nickname nickname = gson.fromJson(response, Nickname.class);
            return nickname;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Changes the Room's questionCooldown attribute.
     * @param room - the Room that needs to be changed.
     * @param cooldown - the new questionCooldown.
     */
    public static void setCooldown(Room room, int cooldown, long nid) {
        room.setQuestionCooldown(cooldown);
        Entity<Room> requestBody = Entity.entity(room, MediaType.APPLICATION_JSON);

        try {
            String response = ClientBuilder.newClient()
                    .target("http://localhost:8080/room/set_cooldown?n=" + nid)
                    .request()
                    .put(requestBody, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a nickname to muted.
     *
     * @param nid the nickname id of the person you want to mute.
     * @param n   your own nickname id (for authorization).
     */
    public static void muteNickname(long nid, long n) {
        String url = "http://localhost:8080/nickname/" + nid + "/mute?n=" + n;

        try {
            String response = ClientBuilder.newClient()
                    .target(url)
                    .request()
                    .get(String.class);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
