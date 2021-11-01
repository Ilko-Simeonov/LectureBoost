package nl.tudelft.oopp.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Upvote")
@Table(name = "upvote")
public class Upvote {
    @Id
    @SequenceGenerator(
            name = "upvote_sequence",
            sequenceName = "upvote_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "upvote_sequence"
    )
    private long id;

    @Column(
            name = "question_id",
            updatable = false,
            nullable = false
    )
    private long questionId;

    @Column(
            name = "nickname_id",
            updatable = false,
            nullable = false
    )
    private long nicknameId;

    @Column(
            name = "upvoted",
            nullable = false
    )
    private boolean upvoted;

    public Upvote() {
    }

    /**
     * Instantiates a new Upvote.
     *
     * @param questionId the question id
     * @param nicknameId the nickname id
     * @param upvoted    whether the question is upvoted
     */
    public Upvote(long questionId, long nicknameId, boolean upvoted) {
        this.questionId = questionId;
        this.nicknameId = nicknameId;
        this.upvoted = upvoted;
    }

    /**
     * Instantiates a new Upvote.
     *
     * @param id         the id
     * @param questionId the question id
     * @param nicknameId the nickname id
     * @param upvoted    whether the question is upvoted
     */
    public Upvote(long id, long questionId, long nicknameId, boolean upvoted) {
        this.id = id;
        this.questionId = questionId;
        this.nicknameId = nicknameId;
        this.upvoted = upvoted;
    }

    public long getId() {
        return id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public long getNicknameId() {
        return nicknameId;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    @Override
    public String toString() {
        return "Upvote{"
                + "id=" + id
                + ", questionId=" + questionId
                + ", nicknameId=" + nicknameId
                + ", upvoted=" + upvoted
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
        Upvote upvote = (Upvote) o;
        return questionId == upvote.questionId
                && nicknameId == upvote.nicknameId
                && upvoted == upvote.upvoted;
    }
}
