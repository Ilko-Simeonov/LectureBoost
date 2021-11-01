package nl.tudelft.oopp.demo.servercontrollers;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollAnswer;
import nl.tudelft.oopp.demo.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/polls")
@RestController
public class PollController {
    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }
    
    @PostMapping("/create")
    public Poll createPoll(@RequestBody Poll poll) {
        return pollService.save(poll);
    }

    @GetMapping("/get")
    public List<Poll> findAll() {
        return pollService.findAll();
    }

    @GetMapping("/{url}/get")
    public List<Poll> findByUrl(@PathVariable String url) {
        return pollService.findByUrl(url);
    }

    @GetMapping("/{url}/get/ordered")
    public List<Poll> findByUrlOrdered(@PathVariable String url) {
        return pollService.findByUrlOrdered(url);
    }

    @GetMapping("/{url}/get/active")
    public List<Poll> findActiveByUrl(@PathVariable String url) {
        return pollService.findActiveByUrl(url);
    }

    @GetMapping("/{pid}/answer/{option}")
    public int answerPoll(@PathVariable long pid, @PathVariable int option, @RequestParam long n) {
        return pollService.answerPoll(pid, option, n);
    }

    @GetMapping("/{pid}/hasanswered")
    public boolean hasAnswered(@PathVariable long pid, @RequestParam long n) {
        return pollService.hasAnswered(pid, n);
    }

    @GetMapping("/getanswers")
    public List<PollAnswer> getAnswers() {
        return pollService.findAllAnswers();
    }

    @GetMapping("/{pid}/getresults")
    public List<Integer> getResults(@PathVariable long pid) {
        return pollService.getResults(pid);
    }

    @DeleteMapping("/{pid}/delete")
    public void deleteById(@PathVariable long pid, @RequestParam long n) {
        pollService.deleteById(pid, n);
    }

    @GetMapping("/{pid}/open")
    public void openPoll(@PathVariable long pid, @RequestParam long n) {
        pollService.setPollActive(pid, true, n);
    }

    @GetMapping("/{pid}/close")
    public void closePoll(@PathVariable long pid, @RequestParam long n) {
        pollService.setPollActive(pid, false, n);
    }

}
