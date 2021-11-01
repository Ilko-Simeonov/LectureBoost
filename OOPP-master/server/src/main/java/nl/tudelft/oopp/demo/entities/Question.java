package nl.tudelft.oopp.demo.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Question")
@Table(name = "question")
public class Question implements CsvData {
    @Id
    @SequenceGenerator(
            name = "question_sequence",
            sequenceName = "question_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private long id;

    @Column(
            name = "room_url",
            updatable = false
    )
    private String roomUrl;

    @Column(
            name = "question",
            nullable = false
    )
    private String question;

    //This will be used later
    /*
    @Column(
            name = "time_asked",
            nullable = false
    )
    private Time timeAsked;
    */

    @Column(
            name = "upvotes",
            nullable = false
    )
    private int upvotes;

    @Column(
            name = "answered",
            nullable = false
    )
    private boolean answered;

    @Column(
            name = "answeredby_id",
            nullable = true
    )
    private int answeredById;

    @Column(
            name = "changedby_id",
            nullable = true
    )
    private int changedById;

    @Column(
            name = "answer",
            nullable = true
    )
    private String answer;

    @Column(
            name = "nickname_id",
            nullable = false
    )
    private long nicknameId;

    public Question() {

    }

    /**
     * Instantiates a new Question.
     *
     * @param question the question
     */
    public Question(String question) {
        this.question = question;
        this.upvotes = 0;
        this.roomUrl = null;
        this.answered = false;
    }

    /**
     * Instantiates a new Question with the room url already set.
     *
     * @param question the question
     * @param url      the room url
     */
    public Question(String question, String url) {
        this.question = question;
        this.upvotes = 0;
        this.roomUrl = url;
        this.answered = false;
    }

    /**
     * Instantiates a new Question with the id, roomUrl, upvotes and answered already set.
     * @param id         the id
     * @param roomUrl    the room url
     * @param question   the question
     * @param upvotes    the upvotes
     * @param nicknameId the nickname Id
     */

    public Question(long id, String roomUrl, String question,
                    int upvotes, long nicknameId, boolean answered) {
        this.id = id;
        this.roomUrl = roomUrl;
        this.question = question;
        this.upvotes = upvotes;
        this.answered = answered;
        this.nicknameId = nicknameId;
    }

    /**
     * Instantiates a new Question with the id, roomUrl, upvotes and answered already set.
     * @param id       the id
     * @param roomUrl  the room url
     * @param question the question
     * @param upvotes  the upvotes
     * @param nicknameId  the nickname Id
     */

    public Question(long id, String roomUrl, String question,
                    int upvotes, int nicknameId, boolean answered, String answer) {
        this.id = id;
        this.roomUrl = roomUrl;
        this.question = question;
        this.upvotes = upvotes;
        this.answered = answered;
        this.answer = answer;
        this.nicknameId = nicknameId;
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setUrl(String url) {
        this.roomUrl = url;
    }

    public long getNicknameId() {
        return nicknameId;
    }

    public void setNicknameId(int nicknameid) {
        this.nicknameId = nicknameid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAnsweredById() {
        return answeredById;
    }

    public void setAnsweredById(int answeredById) {
        this.answeredById = answeredById;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRoomUrl(String roomUrl) {
        this.roomUrl = roomUrl;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    /**
     * Mehtod is used to fetch the data in the question that is answered.
     * @return a list with the information contained in the respective columns
     */
    public List<String> getCsvData() {
        return Arrays.asList(
                String.valueOf(this.getId()),
                this.getRoomUrl(),
                this.getQuestion(),
                String.valueOf(this.getUpvotes()),
                String.valueOf(this.isAnswered()),
                String.valueOf(this.getNicknameId()));
    }

    @Override
    public String toString() {
        return "Question{id=" + id
                + ", roomUrl='" + roomUrl + '\''
                + ", question='" + question + '\''
                + ", nicknameId='" + nicknameId + '\''
                + ", answered='" + answered + '\''
                + ", answer='" + answer + '\''
                + ", answeredById='" + answeredById + '\''
                + ", upvotes=" + upvotes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question1 = (Question) o;
        return upvotes == question1.upvotes
                && answered == question1.answered
                && nicknameId == question1.nicknameId
                && Objects.equals(roomUrl, question1.roomUrl)
                && Objects.equals(question, question1.question)
                && Objects.equals(answer, question1.answer);
    }

}
