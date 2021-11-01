package nl.tudelft.oopp.demo.scheduler;

import nl.tudelft.oopp.demo.conts.CommonConts;
import nl.tudelft.oopp.demo.services.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FeedbackScheduler {

    @Autowired
    private FeedbackService feedbackService;

    //(CommonConts.DELETE_FEEDBACK_SCHEDULAR_INTERVAL_IN_MINUTES * 1000 * X)
    //Where X is the amount of seconds in between method calls
    @Scheduled(fixedRate = CommonConts.DELETE_FEEDBACK_SCHEDULAR_INTERVAL_IN_MINUTES * 1000 * 15)
    public void reportCurrentTime() {
        feedbackService.deleteOlderFeedbacks(5);
        System.out.println("Old Feedbacks deleted.....");
    }
}
