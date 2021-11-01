package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

public class Nickname {
    private long id;
    private String name;
    private String roomUrl;
    private boolean moderator;
    private boolean muted;

    /**
     * Instantiates a new Nickname.
     *
     * @param name the name
     * @param roomUrl  the room_url
     */
    public Nickname(String name, String roomUrl) {
        this.name = name;
        this.roomUrl = roomUrl;
        this.moderator = false;
        this.muted = false;
    }

    /**
     * Instantiates a new Nickname.
     *
     * @param id        the id
     * @param name      the name
     * @param roomUrl   the room_url
     * @param moderator whether the person is a moderator
     */
    public Nickname(long id, String name, String roomUrl, boolean moderator, boolean muted) {
        this.id = id;
        this.name = name;
        this.roomUrl = roomUrl;
        this.moderator = moderator;
        this.muted = muted;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public boolean isModerator() {
        return moderator;
    }

    public boolean isMuted() {
        return muted;
    }

    @Override
    public String toString() {
        return "Nickname{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", roomUrl='" + roomUrl + '\''
                + ", moderator=" + moderator
                + ", muted=" + muted
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
        Nickname nickname = (Nickname) o;
        return moderator == nickname.moderator
                && muted == nickname.muted
                && Objects.equals(name, nickname.name)
                && Objects.equals(roomUrl, nickname.roomUrl);
    }
}
