package nl.tudelft.oopp.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.PollAnswer;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.PollAnswerRepository;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PollService {
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;
    private final NicknameRepository nicknameRepository;

    /**
     * Instantiates a new Poll service.
     *
     * @param pollRepository       the poll repository
     * @param pollAnswerRepository the poll answer repository
     * @param nicknameRepository   the nickname repository
     */
    @Autowired
    public PollService(@Qualifier("PollRepository") PollRepository pollRepository,
                       @Qualifier("PollAnswerRepository") PollAnswerRepository pollAnswerRepository,
                       @Qualifier("NicknameRepository") NicknameRepository nicknameRepository) {
        this.pollRepository = pollRepository;
        this.pollAnswerRepository = pollAnswerRepository;
        this.nicknameRepository = nicknameRepository;
    }

    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    public Poll save(Poll poll) {
        return pollRepository.save(poll);
    }

    public List<Poll> findByUrl(String url) {
        return pollRepository.findByUrl(url);
    }

    public List<Poll> findByUrlOrdered(String url) {
        return pollRepository.findByUrlOrdered(url);
    }

    public List<Poll> findActiveByUrl(String url) {
        return pollRepository.findActiveByUrl(url);
    }

    /**
     * Answers a poll.
     * If the person has already answered that poll,
     * (thus a record already exists for that pollId and nicknameId),
     * then it updates their answer. Otherwise it saves a new record.
     *
     * @param pid    the poll id.
     * @param option the option the student chose.
     * @param nid    the nickname id.
     */
    public int answerPoll(long pid, int option, long nid) {
        Optional<PollAnswer> pa = pollAnswerRepository.findByPollIdAndNicknameId(pid, nid);
        Optional<Poll> p = pollRepository.findById(pid);

        //If there doesn't exist a poll with the pid, return.
        if (p.isEmpty()) {
            return -1;
        }

        Poll poll = p.get();

        if (option > poll.amountOfOptions() || !poll.isActive()) {
            //If e.g option 5 is chosen, but there are only 4 options, return.
            //If poll is not active, return.
            return -1;
        }

        if (pa.isPresent()) {
            PollAnswer pollAnswer = pa.get();
            pollAnswerRepository.updateById(pollAnswer.getId(), option);
        } else {
            pollAnswerRepository.save(new PollAnswer(0, pid, nid, option));
        }
        return option;
    }

    public boolean hasAnswered(long pid, long nid) {
        Optional<PollAnswer> pa = pollAnswerRepository.findByPollIdAndNicknameId(pid, nid);
        return pa.isPresent();
    }

    public List<PollAnswer> findAllAnswers() {
        return pollAnswerRepository.findAll();
    }

    /**
     * Returns list per option of how many percent of people answered with that option.
     *
     * @param pid the poll_id
     * @return the percentages
     */
    public List<Integer> getResults(long pid) {
        Optional<Poll> pollOptional = pollRepository.findById(pid);
        if (pollOptional.isEmpty()) {
            return null;
        }
        Poll poll = pollOptional.get();

        List<Integer> amounts = new ArrayList<>();

        for (int i = 1; i <= poll.amountOfOptions(); i++) {
            amounts.add(pollAnswerRepository.getAmountOfAnswers(pid, i));
        }

        double totalAnswers = 0;
        for (int i : amounts) {
            totalAnswers += i;
        }
        List<Integer> percentages = new ArrayList<>();

        for (int i : amounts) {
            if (totalAnswers == 0) {
                percentages.add(0);
                continue;
            }
            percentages.add((int) Math.round(i / totalAnswers * 100));
        }

        return percentages;
    }

    /**
     * Delete poll by id.
     *
     * @param pid the poll_id
     * @param nid the nickname_id
     */
    public void deleteById(long pid, long nid) {
        Optional<Poll> poll = pollRepository.findById(pid);
        if (poll.isEmpty()) {
            return;
        }
        if (isAuthorized(nid, poll.get().getRoomUrl())) {
            pollRepository.deleteById(pid);
            pollAnswerRepository.deleteByPid(pid);
        }
    }

    /**
     * Sets poll as active.
     *
     * @param pid    the poll_id
     * @param active whether the poll should be set to active or not
     * @param nid    the nickname_id
     */
    public void setPollActive(long pid, boolean active, long nid) {
        Optional<Poll> poll = pollRepository.findById(pid);
        if (poll.isEmpty()) {
            return;
        }
        if (isAuthorized(nid, poll.get().getRoomUrl())) {
            pollRepository.setActive(pid, active);
        }
    }

    private boolean isAuthorized(long nid, String roomUrl) {
        Optional<Nickname> nickname = nicknameRepository.findById(nid);
        return (nickname.isPresent()
                && nickname.get().isModerator()
                && nickname.get().getRoomUrl().equals(roomUrl));
    }
}
