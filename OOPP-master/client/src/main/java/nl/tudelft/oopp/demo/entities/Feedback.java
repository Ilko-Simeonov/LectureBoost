package nl.tudelft.oopp.demo.entities;

import java.util.Date;
import java.util.Objects;

public class Feedback {
    private long id;
    private String roomUrl;
    private String message;
    private FeedbackType feedbackType;
    private long nicknameId;

    public Feedback() {

    }

    public Feedback(String roomUrl, String message) {
        this.roomUrl = roomUrl;
        this.message = message;
    }

    /**
     * Constructor for feedback.
     * @param id id of feedback
     * @param roomUrl room url
     * @param message message of feedback
     */
    public Feedback(long id, String roomUrl, String message) {
        this.id = id;
        this.roomUrl = roomUrl;
        this.message = message;
    }

    /**
     * Constructor for feedback.
     * @param roomUrl url of room
     * @param message message of feedback
     * @param feedbackType type of feedback
     */
    public Feedback(String roomUrl, String message, FeedbackType feedbackType) {
        this.roomUrl = roomUrl;
        this.message = message;
        this.feedbackType = feedbackType;
    }

    /**
     * Constructor for feedback.
     * @param roomUrl url of room
     * @param message message of feedback
     * @param feedbackType type of feedback
     * @param nicknameId id of user giving feedback
     */
    public Feedback(String roomUrl, String message, FeedbackType feedbackType,
                    long nicknameId) {
        this.roomUrl = roomUrl;
        this.message = message;
        this.feedbackType = feedbackType;
        this.nicknameId = nicknameId;
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
                + ", nicknameId='" + nicknameId + '\''
                + ", message='" + message + '\''
                + ", feedbackType='" + feedbackType + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feedback feedback = (Feedback) o;
        return nicknameId == feedback.nicknameId
                && Objects.equals(roomUrl, feedback.roomUrl)
                && Objects.equals(message, feedback.message)
                && feedbackType == feedback.feedbackType;
    }
}
