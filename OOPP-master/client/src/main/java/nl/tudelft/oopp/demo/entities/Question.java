package nl.tudelft.oopp.demo.entities;

import java.util.Objects;

public class Question {
    private long id;
    private String roomUrl;
    private String question;
    private int upvotes;
    private boolean answered;
    private String answer;
    private long nicknameId;
    private boolean upvoted;

    /**
     * Instantiates a new Question.
     */
    public Question() {

    }

    /**
     * Instantiates a new Question with only the question set.
     *
     * @param question   the question
     * @param nicknameId the nickname id
     */
    public Question(String question, long nicknameId) {
        this.question = question;
        this.upvotes = 0;
        this.roomUrl = null;
        this.nicknameId = nicknameId;
        this.upvoted = false;
    }

    /**
     * Instantiates a new Question.
     *
     * @param id         the id
     * @param roomUrl    the room url
     * @param question   the question
     * @param upvotes    the upvotes
     * @param answered   whether the question is marked as answered
     * @param nicknameId the nickname id
     */
    public Question(long id, String roomUrl, String question,
                    int upvotes, boolean answered, String answer, long nicknameId) {
        this.id = id;
        this.roomUrl = roomUrl;
        this.question = question;
        this.upvotes = upvotes;
        this.answered = answered;
        this.answer = answer;
        this.nicknameId = nicknameId;
        this.upvoted = false;
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", roomUrl='" + roomUrl + '\''
                + ", question='" + question + '\''
                + ", upvotes=" + upvotes + '\''
                + ", answered=" + answered + '\''
                + ", answer=" + answer + '\''
                + ", nicknameId=" + nicknameId
                + ", upvoted=" + upvoted
                + '}';
    }

    public long getId() {
        return id;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public String getQuestion() {
        return question;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public boolean isAnswered() {
        return answered;
    }

    public long getNicknameId() {
        return nicknameId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    } 
    
    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
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
                && upvoted == question1.upvoted
                && Objects.equals(roomUrl, question1.roomUrl)
                && Objects.equals(question, question1.question)
                && Objects.equals(answer, question1.answer);
    }
}
