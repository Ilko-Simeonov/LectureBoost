package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Nickname")
@Table(name = "nickname")
public class Nickname {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private long id;

    @Column(
            name = "name",
            updatable = true
    )
    private String name;

    @Column(
            name = "room_url",
            updatable = false
    )
    private String roomUrl;

    @Column(
            name = "moderator",
            updatable = true
    )
    private boolean moderator;

    @Column(
            name = "active",
            updatable = true
    )
    private boolean active;
    
    @Column(
            name = "muted",
            updatable = true
    )
    private boolean muted;


    public Nickname() {

    }

    /**
     * Instantiates a new Nickname with the name and room url already set.
     *
     * @param name      the name
     * @param roomUrl   the room url
     * @param moderator the status of user
     */
    public Nickname(String name, String roomUrl, boolean moderator) {
        this.name = name;
        this.roomUrl = roomUrl;
        this.moderator = moderator;
        this.active = true;
        this.muted = false;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public boolean isModerator() {
        return moderator;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomUrl(String roomUrl) {
        this.roomUrl = roomUrl;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    @Override
    public String toString() {
        return "Nickname{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", roomUrl='" + roomUrl + '\''
                + ", moderator=" + moderator
                + ", active=" + active
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
                && active == nickname.active
                && Objects.equals(name, nickname.name)
                && Objects.equals(roomUrl, nickname.roomUrl);
    }
}
