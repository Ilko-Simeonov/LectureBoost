package nl.tudelft.oopp.demo.entities;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Room")
@Table(name = "room")
public class Room {
    @Id
    @SequenceGenerator(
            name = "room_sequence", sequenceName = "room_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_sequence")

    private long id;
    //URL to access the room (not the whole link, just the url parameter)
    //Take for example a youtube video, then in our DB this url would be stored as "dQw4w9WgXcQ"
    @Column(
            name = "url",
            nullable = false
    )
    private String url;

    //Room name, e.g: "OOPP Lecture 5"
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    //Time startTime: The lecture opens at "14:00" (includes date)
    @Column(
            name = "starttime",
            nullable = true
    )
    private long startTime;

    //Time endTime: The lecture is not open after "15:30:00" (includes date)
    @Column(
            name = "endtime",
            nullable = true
    )
    private long endTime;

    @Column(
        name = "moderator_password",
        nullable = false
    )
    private String moderatorPassword;


    @Column(
            name = "open",
            nullable = false
    )
    private boolean open;

    @Column(
            name = "question_cooldown",
            nullable = false
    )
    private int questionCooldown;

    public Room() {

    }

    /**
     * Instantiates a new Room.
     *
     * @param name              the name or the room
     * @param url               the url
     * @param open              true if the room should be open at the point of creation
     * @param moderatorPassword the moderator password
     * @param startTime         the long of the start time
     * @param endTime           the long of the end time
     */
    public Room(String name, String url, boolean open, String moderatorPassword,
                long startTime, long endTime, int questionCooldown) {
        this.name = name;
        this.url = url;
        this.open = open;
        this.moderatorPassword = moderatorPassword;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questionCooldown = questionCooldown;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getModeratorPassword() {
        return moderatorPassword;
    }

    public void setModeratorPassword(String moderatorPassword) {
        this.moderatorPassword = moderatorPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuestionCooldown() {
        return questionCooldown;
    }

    public void setQuestionCooldown(int questionCooldown) {
        this.questionCooldown = questionCooldown;
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
        return startTime == room.startTime
                && endTime == room.endTime
                && open == room.open
                && Objects.equals(url, room.url)
                && Objects.equals(name, room.name)
                && Objects.equals(moderatorPassword, room.moderatorPassword);
    }

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("Europe/Amsterdam");
        c.setTimeZone(tz);
        c.setTimeInMillis(startTime);

        String start = calendarToString(c);

        c.setTimeInMillis(endTime);
        String end = calendarToString(c);

        return "Room{roomUrl='"
                + url + '\''
                + ", name='" + name + '\''
                + ", open='" + open + '\''
                + ", starttime='" + start + '\''
                + ", endtime='" + end + '\''
                + '}';
    }

    /**
     * Changes calendar into readable string.
     * Format: DD-MMM-YYYY @ hh:mm
     * e.g: 07-APR-2021 @ 15:00
     *
     * @param c the calendar
     * @return the readable string
     */
    public static String calendarToString(Calendar c) {
        int monthInt = c.get(Calendar.MONTH) + 1;
        String month;
        switch (monthInt) {
            case 1:
                month =  "JAN";
                break;
            case 2:
                month =  "FEB";
                break;
            case 3:
                month =  "MAR";
                break;
            case 4:
                month =  "APR";
                break;
            case 5:
                month =  "MAY";
                break;
            case 6:
                month =  "JUN";
                break;
            case 7:
                month =  "JUL";
                break;
            case 8:
                month =  "AUG";
                break;
            case 9:
                month =  "SEP";
                break;
            case 10:
                month =  "OKT";
                break;
            case 11:
                month =  "NOV";
                break;
            default:
                month =  "DEC";
        }

        String day = "" + c.get(Calendar.DAY_OF_MONTH);
        String hour = "" + c.get(Calendar.HOUR_OF_DAY);
        String min =  "" + c.get(Calendar.MINUTE);
        
        if (day.length() < 2) {
            day = "0" + day;
        }
        if (hour.length() < 2) {
            hour = "0" + hour;
        }
        if (min.length() < 2) {
            min = "0" + min;
        }

        String year = "" + c.get(Calendar.YEAR);

        return (day + "-" + month + "-" + year + " @ " + hour + ":" + min);
    }
}
