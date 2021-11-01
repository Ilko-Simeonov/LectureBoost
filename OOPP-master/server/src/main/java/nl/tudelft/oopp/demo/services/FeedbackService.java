package nl.tudelft.oopp.demo.services;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(@Qualifier("FeedbackRepository") FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * If the same user submits feedback again we just update the existing feedback.
     * @param feedback feedback submitted
     * @return saves feedback
     */
    public Feedback save(Feedback feedback) {
        feedback.setCreatedTime(System.currentTimeMillis());
        List<Feedback> feedbacks = feedbackRepository.findByUrlAndNickNameId(feedback.getRoomUrl(),
                feedback.getNicknameId());
        if (feedbacks.size() > 0) {
            Feedback feedBackEntry = feedbacks.get(0);
            feedBackEntry.setFeedbackType(feedback.getFeedbackType());
            feedBackEntry.setMessage(feedback.getMessage());
            return feedbackRepository.save(feedBackEntry);
        } else {
            return feedbackRepository.save(feedback);
        }
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> findByUrl(String room) {
        return feedbackRepository.findByUrl(room);
    }

    public Integer countTooSlow(String room) {
        return feedbackRepository.countTooSlow(room);
    }

    public Integer countTooFast(String room) {
        return feedbackRepository.countTooFast(room);
    }

    /**
     * Delete older feedbacks.
     *
     * @param deleteAfterMin the delete after x min
     */
    public void deleteOlderFeedbacks(double deleteAfterMin) {
        long deleteAfterMS = (long) (deleteAfterMin * 60000);
        long tooOldTime = System.currentTimeMillis() - deleteAfterMS;
        feedbackRepository.deleteOlderFeedbacks(tooOldTime);
    }


}
