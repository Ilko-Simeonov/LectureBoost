package nl.tudelft.oopp.demo.servercontrollers;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Question controller.
 */
@RequestMapping("/question")
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    /**
     * Saves a question (after setting its url attribute).
     *
     * @param question the question (Request body)
     * @param room     the room url (PathVariable)
     * @return the question back to the client
     */
    @PostMapping("/{room}/create")
    Question save(@RequestBody Question question, @PathVariable String room) {
        question.setUrl(room);
        return questionService.save(question);
    }

    @GetMapping("/get/{qid}")
    public Optional<Question> findById(@PathVariable long qid) {
        return questionService.findById(qid);
    }

    @GetMapping("/{room}/get")
    public List<Question> findByUrl(@PathVariable String room) {
        return questionService.findByUrl(room);
    }

    @GetMapping("/{room}/{nickname_id}/get")
    public List<Question> findByUrlAndUser(@PathVariable String room,
                                           @PathVariable int nicknameid) {
        return questionService.findByUrlAndUserID(room, nicknameid);
    }

    @GetMapping("/get")
    public List<Question> findAll() {
        return questionService.findAll();
    }

    @DeleteMapping("/{qid}/delete")
    public void deleteById(@PathVariable long qid, @RequestParam long n) {
        questionService.deleteById(qid, n);
    }

    @GetMapping("/upvote/{qid}")
    public void upvoteById(@PathVariable long qid, @RequestParam long n) {
        questionService.upvoteById(qid, n);
    }

    @GetMapping("/get/ordered")
    public List<Question> findAllByUpvotesDesc() {
        return questionService.findAllByUpvotesDesc();
    }

    /**
     * Find all questions with a certain url, and return those ordered by upvotes (descending).
     *
     * @param room the roomUrl
     * @return the list of ordered questions for that url
     */
    @GetMapping("/{room}/get/ordered")
    public List<Question> findByUrlOrdered(@PathVariable String room) {
        return questionService.findByUrlOrdered(room);
    }

    @GetMapping("/get/answered")
    public List<Question> findAnswered() {
        return questionService.findAnswered();
    }

    /**
     * The response is a csv file containing the answered questions.
     * @return the csv file
     */
    @GetMapping("/get/answered_csv")
    public ResponseEntity<Resource> getAnsweredCsv() {
        return getResponseEntity(
                questionService.convertToCsvByteArray(questionService.findAnswered()),
                "answered_questions.csv"
        );
    }

    @GetMapping("/{room}/get/answered")
    public List<Question> findAnsweredByUrl(@PathVariable String room) {
        return questionService.findAnsweredByUrl(room);
    }

    /**
     * Gives the answered questions for the given room in a scv format.
     * @param room the room url
     * @return the answered questions for the current room in a scv format
     */
    @GetMapping("/{room}/get/answered_csv")
    public  ResponseEntity<Resource> getAnsweredCsvByUrl(@PathVariable String room) {
        System.out.println("Download started....");
        return getResponseEntity(
                questionService.convertToCsvByteArray(questionService.findAnsweredByUrl(room)),
                "answered_questions_by_url(" + room + ").csv"
        );
    }

    @GetMapping("/{qid}/markanswered")
    public void markAnsweredById(@PathVariable long qid, @RequestParam long n) {
        questionService.markAnsweredById(qid, n);
    }

    @PutMapping("/{qid}/{nid}/answer")
    public void questionAnswer(@PathVariable long qid, @PathVariable long nid,
                               @RequestBody String answer) {
        questionService.questionAnswer(qid, nid, answer);
    }

    @PutMapping("/{qid}/{nid}/edit")
    public void questionEdit(@PathVariable long qid, @PathVariable long nid,
                             @RequestBody String change) {
        questionService.questionEdit(qid, nid, change);
    }

    private ResponseEntity<Resource> getResponseEntity(ByteArrayInputStream inputStream,
                                                       String fileName) {
        InputStreamResource file = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @GetMapping("/{qid}/hasupvoted")
    public boolean hasUpvoted(@PathVariable long qid, @RequestParam long n) {
        return questionService.hasUpvoted(qid, n);
    }

}
