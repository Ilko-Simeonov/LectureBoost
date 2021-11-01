package nl.tudelft.oopp.demo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Feedback")
@Table(name = "feedback")
public class Feedback {

    @Id
    @SequenceGenerator(
            name = "feedback_sequence",
            sequenceName = "feedback_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedback_sequence"
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
            name = "message",
            nullable = false
    )
    private String message;

    @Column(
            name = "feedbackType",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @Column(
            name = "created_time",
            nullable = false
    )
    private long createdTime;

    @Column(
            name = "nickname_id",
            nullable = false
    )
    private long nicknameId;


    public Feedback() {

    }

    /**
     * Instantiates a new Feedback.
     *
     * @param roomUrl the room url
     * @param message the message
     */
    public Feedback(String roomUrl, String message) {
        this.roomUrl = roomUrl;
        this.message = message;
        this.createdTime = System.currentTimeMillis();
    }

    /**
     * Instantiates a new Feedback.
     *
     * @param roomUrl      the room url
     * @param message      the message
     * @param feedbackType the feedback type
     * @param nicknameId   the nickname id
     */
    public Feedback(String roomUrl, String message, FeedbackType feedbackType, long nicknameId) {
        this.roomUrl = roomUrl;
        this.message = message;
        this.feedbackType = feedbackType;
        this.nicknameId = nicknameId;
        this.createdTime = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public void setRoomUrl(String roomUrl) {
        this.roomUrl = roomUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getNicknameId() {
        return nicknameId;
    }

    public void setNicknameId(long nicknameId) {
        this.nicknameId = nicknameId;
    }

    @Override
    public String toString() {
        return "Feedback{"
                + "id=" + id
                + ", roomUrl='" + roomUrl + '\''
                + ", message='" + message + '\''
                + ", nickname='" + nicknameId + '\''
                + ", feedbackType='" + feedbackType + '\''
                + ", createdTime='" + createdTime + '\''
                + '}';
    }
}
