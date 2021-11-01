package nl.tudelft.oopp.demo.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Objects;

public class Room {
    private String name;
    private String url;
    private String moderatorPassword;
    private boolean open;
    private long startTime;
    private long endTime;
    private int questionCooldown;

    /**
     * Instantiates a new Room.
     *
     * @param name              the name or the room
     * @param open              true if the room should be open at the point of creation
     * @param moderatorPassword the moderator password
     * @param startTime         the start time (milliseconds)
     * @param endTime           the end time (milliseconds)
     */
    public Room(String name, boolean open, String moderatorPassword,
                long startTime, long endTime) {
        this.name = name;
        this.open = open;
        this.moderatorPassword = moderatorPassword;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questionCooldown = 30;
    }

    /**
     * Instantiates a new Room.
     *
     * @param name              the name or the room
     * @param url               the url
     * @param open              true if the room should be open at the point of creation
     * @param moderatorPassword the moderator password
     * @param startTime         the start time
     * @param endTime           the end time
     */
    public Room(String name, String url, boolean open, String moderatorPassword,
                long startTime, long endTime) {
        this.name = name;
        this.url = url;
        this.open = open;
        this.moderatorPassword = moderatorPassword;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questionCooldown = 30;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getModeratorPassword() {
        return moderatorPassword;
    }

    public void setModeratorPassword(String moderatorPassword) {
        this.moderatorPassword = moderatorPassword;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getQuestionCooldown() {
        return questionCooldown;
    }

    public void setQuestionCooldown(int questionCooldown) {
        this.questionCooldown = questionCooldown;
    }

    @Override
    public String toString() {
        return "Room{"
                + "name='" + name + '\''
                + ", url='" + url + '\''
                + ", moderatorPassword='" + moderatorPassword + '\''
                + ", open=" + open
                + ", starttime='" + startTime + '\''
                + ", endtime='" + endTime + '\''
                + ", questionCooldown=" + questionCooldown
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
        Room room = (Room) o;
        return  open == room.open
                && Objects.equals(name, room.name)
                && Objects.equals(url, room.url)
                && Objects.equals(moderatorPassword, room.moderatorPassword)
                && Objects.equals(startTime, room.startTime)
                && Objects.equals(endTime, room.endTime);
    }
}
