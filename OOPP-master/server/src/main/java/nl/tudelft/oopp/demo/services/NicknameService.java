package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class NicknameService {
    private final NicknameRepository nicknameRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public NicknameService(@Qualifier("NicknameRepository") NicknameRepository nicknameRepository,
                           @Qualifier("RoomRepository") RoomRepository roomRepository) {
        this.nicknameRepository = nicknameRepository;
        this.roomRepository = roomRepository;
    }


    /**
     * Finds all nicknames for a url.
     *
     * @param url the url
     * @return list of nicknames for the url
     */
    public List<Nickname> findByUrl(String url) {
        return nicknameRepository.findByUrl(url);
    }

    /**
     * Save a nickname.
     *
     * @param nickname the nickname
     * @return the saved nickname (with id)
     */
    public Nickname save(Nickname nickname, String p) {
        Optional<Room> r = roomRepository.findByUrl(nickname.getRoomUrl());
        if (r.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room doesn't exist");
        }
        Room room = r.get();

        if (p.equals("")) {
            nickname.setModerator(false);
            if (!room.isOpen()) {
                return null;
            }
        } else if (room.getModeratorPassword().equals(p) || room.getModeratorPassword() == null) {
            nickname.setModerator(true);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Incorrect moderator password");
        }

        Optional<Nickname> n = nicknameRepository.findByUrlAndName(
                nickname.getRoomUrl(),
                nickname.getName()
        );

        if (n.isPresent()) {
            //Can't save a nickname if that nickname already exists for that url.
            return null;
        }

        nickname.setActive(true);
        return nicknameRepository.save(nickname);
    }

    /**
     * Changes the nickname.
     * @param n - the updated Nickname.
     */
    public void change(Nickname n) {
        long nid = n.getId();
        String newNickname = n.getName();
        nicknameRepository.change(nid, newNickname);
    }

    public List<Nickname> findAll() {
        return nicknameRepository.findAll();
    }

    public Optional<Nickname> findById(long id) {
        return nicknameRepository.findById(id);
    }

    public void leaveById(long id) {
        nicknameRepository.leaveById(id, false);
    }

    /**
     * Finds all moderators in a room for a url.
     *
     * @param url the url
     * @return list of moderators for the url
     */
    public List<Nickname> findModeratorsByUrl(String url) {
        return nicknameRepository.findModeratorsByUrl(url);
    }

    public Optional<Nickname> findByUrlAndName(String url, String name) {
        return nicknameRepository.findByUrlAndName(url, name);
    }

    public int amountOfParticipants(String roomUrl) {
        return nicknameRepository.amountOfParticipants(roomUrl);
    }

    /**
     * Set a nickname to muted.
     *
     * @param nid the nickname id of the person that will be muted.
     * @param n   the nickname id of the person that wants to mute the other (for authorization).
     */
    public void muteNickname(long nid, long n) {
        Optional<Nickname> moderatorOptional = nicknameRepository.findById(n);
        Optional<Nickname> mutedOptional = nicknameRepository.findById(nid);

        if (moderatorOptional.isEmpty() || mutedOptional.isEmpty()) {
            return;
        }

        Nickname moderator = moderatorOptional.get();
        Nickname muted = mutedOptional.get();

        if (muted.isModerator()) {
            //Moderators cannot be muted.
            return;
        }

        if (moderator.isModerator() && moderator.getRoomUrl().equals(muted.getRoomUrl())) {
            nicknameRepository.muteById(nid);
        }
    }
}
