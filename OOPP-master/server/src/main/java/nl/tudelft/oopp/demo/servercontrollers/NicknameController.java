package nl.tudelft.oopp.demo.servercontrollers;

import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.services.NicknameService;
import nl.tudelft.oopp.demo.services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/nickname")
@RestController
public class NicknameController {
    private final NicknameService nicknameService;

    @Autowired
    public NicknameController(NicknameService nicknameService) {
        this.nicknameService = nicknameService;
    }

    /**
     * Saves a nickname in room.
     *
     * @param nickname the nickname (Request body)
     */
    @PostMapping("/create")
    Nickname save(@RequestBody Nickname nickname, @RequestParam String p) {
        return nicknameService.save(nickname, p);
    }

    @PutMapping("/change")
    public void change(@RequestBody Nickname n) {
        nicknameService.change(n);
    }

    @GetMapping("/get")
    public List<Nickname> findAll() {
        return nicknameService.findAll();
    }

    @GetMapping("/get/{id}")
    public Optional<Nickname> findById(@PathVariable long id) {
        return nicknameService.findById(id);
    }

    @GetMapping("/leave/{id}")
    public void leaveById(@PathVariable long id) {
        nicknameService.leaveById(id);
    }

    @GetMapping("/{room}/get")
    public List<Nickname> findByUrl(@PathVariable String room) {
        return nicknameService.findByUrl(room);
    }

    @GetMapping("/{room}/get/{name}")
    public Optional<Nickname> findByUrlAndName(@PathVariable String room,
                                               @PathVariable String name) {
        return nicknameService.findByUrlAndName(room, name);
    }

    @GetMapping("/{room}/moderators/get")
    public List<Nickname> findModeratorsByUrl(@PathVariable String room) {
        return nicknameService.findModeratorsByUrl(room);
    }

    @GetMapping("/{room}/participants")
    public int amountOfParticipants(@PathVariable String room) {
        return nicknameService.amountOfParticipants(room);
    }

    @GetMapping("/{nid}/mute")
    public void muteNickname(@PathVariable long nid, @RequestParam long n) {
        nicknameService.muteNickname(nid, n);
    }
}
