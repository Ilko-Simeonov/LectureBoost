package nl.tudelft.oopp.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "PollAnswer")
@Table(name = "pollanswer")
public class PollAnswer {
    @Id
    @SequenceGenerator(
            name = "pollanswer_sequence",
            sequenceName = "pollanswer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pollanswer_sequence"
    )
    private long id;

    @Column(
            name = "poll_id",
            updatable = false,
            nullable = false
    )
    private long pollId;

    @Column(
            name = "nickname_id",
            updatable = false,
            nullable = false
    )
    private long nicknameId;

    @Column(
            name = "answer",
            nullable = false
    )
    private int answer;

    /**
     * Instantiates a new PollAnswer.
     */
    public PollAnswer() {

    }

    /**
     * Instantiates a new Poll answer.
     *
     * @param id         the id
     * @param pollId     the poll id
     * @param nicknameId the nickname id
     * @param answer     the chosen answer
     */
    public PollAnswer(long id, long pollId, long nicknameId, int answer) {
        this.id = id;
        this.pollId = pollId;
        this.nicknameId = nicknameId;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public long getPollId() {
        return pollId;
    }

    public long getNicknameId() {
        return nicknameId;
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "PollAnswer{"
                + "id=" + id
                + ", pollId='" + pollId + '\''
                + ", nicknameId='" + nicknameId + '\''
                + ", answer=" + answer
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
        PollAnswer that = (PollAnswer) o;
        return pollId == that.pollId
                && nicknameId == that.nicknameId
                && answer == that.answer;
    }
}
