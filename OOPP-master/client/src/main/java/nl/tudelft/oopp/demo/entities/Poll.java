package nl.tudelft.oopp.demo.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Poll {
    private long id;
    private String roomUrl;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;
    private boolean active;
    private boolean answered;

    /**
     * Instantiates a new Poll.
     *
     * @param id       the id
     * @param roomUrl  the room url
     * @param title    the title
     * @param option1  the 1st option
     * @param option2  the 2nd option
     * @param option3  the 3rd option
     * @param option4  the 4th option
     * @param option5  the 5th option
     * @param option6  the 6th option
     * @param active   whether the poll is active
     * @param answered whether the poll is answered (by this client)
     */
    public Poll(long id, String roomUrl, String title, String option1, String option2,
                String option3, String option4, String option5,
                String option6, boolean active, boolean answered) {
        this.id = id;
        this.roomUrl = roomUrl;
        this.title = title;

        List<String> options = new ArrayList<>();

        if (option1 != null) {
            options.add(option1);
        }
        if (option2 != null) {
            options.add(option2);
        }
        if (option3 != null) {
            options.add(option3);
        }
        if (option4 != null) {
            options.add(option4);
        }
        if (option5 != null) {
            options.add(option5);
        }
        if (option6 != null) {
            options.add(option6);
        }

        if (!options.isEmpty()) {
            this.option1 = options.remove(0);
        }
        if (!options.isEmpty()) {
            this.option2 = options.remove(0);
        }
        if (!options.isEmpty()) {
            this.option3 = options.remove(0);
        }
        if (!options.isEmpty()) {
            this.option4 = options.remove(0);
        }
        if (!options.isEmpty()) {
            this.option5 = options.remove(0);
        }
        if (!options.isEmpty()) {
            this.option6 = options.remove(0);
        }

        this.active = active;
        this.answered = answered;
    }

    public long getId() {
        return id;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getOption5() {
        return option5;
    }

    public String getOption6() {
        return option6;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    /**
     * Returns the amount of options a poll has.
     *
     * @return the amount of options.
     */
    public int amountOfOptions() {
        int i = 0;
        if (this.option1 != null) {
            i++;
        }
        if (this.option2 != null) {
            i++;
        }
        if (this.option3 != null) {
            i++;
        }
        if (this.option4 != null) {
            i++;
        }
        if (this.option5 != null) {
            i++;
        }
        if (this.option6 != null) {
            i++;
        }

        return i;
    }

    @Override
    public String toString() {
        return "Poll{"
                + "id=" + id
                + ", roomUrl='" + roomUrl + '\''
                + ", title='" + title + '\''
                + ", option1='" + option1 + '\''
                + ", option2='" + option2 + '\''
                + ", option3='" + option3 + '\''
                + ", option4='" + option4 + '\''
                + ", option5='" + option5 + '\''
                + ", option6='" + option6 + '\''
                + ", active=" + active
                + ", answered=" + answered
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
        Poll poll = (Poll) o;
        return active == poll.active
                && answered == poll.answered
                && Objects.equals(roomUrl, poll.roomUrl)
                && Objects.equals(title, poll.title)
                && Objects.equals(option1, poll.option1)
                && Objects.equals(option2, poll.option2)
                && Objects.equals(option3, poll.option3)
                && Objects.equals(option4, poll.option4)
                && Objects.equals(option5, poll.option5)
                && Objects.equals(option6, poll.option6);
    }
}
