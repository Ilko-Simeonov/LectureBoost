package nl.tudelft.oopp.demo.servercontrollers;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.services.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/feedback")
@RestController
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/create")
    Feedback save(@RequestBody Feedback feedback) {
        return feedbackService.save(feedback);
    }

    @GetMapping("/get")
    public List<Feedback> findAll() {
        return feedbackService.findAll();
    }

    @GetMapping("/{room}/get")
    public List<Feedback> findByUrl(@PathVariable String room) {
        return feedbackService.findByUrl(room);
    }

    @GetMapping("/{room}/get/slow")
    public Integer countTooSlow(@PathVariable String room) {
        return feedbackService.countTooSlow(room);
    }

    @GetMapping("/{room}/get/fast")
    public Integer countTooFast(@PathVariable String room) {
        return feedbackService.countTooFast(room);
    }

}
