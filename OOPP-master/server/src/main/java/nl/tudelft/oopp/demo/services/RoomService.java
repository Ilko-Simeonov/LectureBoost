package nl.tudelft.oopp.demo.services;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.util.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final NicknameRepository nicknameRepository;

    @Autowired
    public RoomService(@Qualifier("RoomRepository") RoomRepository roomRepository,
                       @Qualifier("NicknameRepository") NicknameRepository nicknameRepository) {
        this.roomRepository = roomRepository;
        this.nicknameRepository = nicknameRepository;
    }

    /**
     * Method should change the room url from null
     * to the new unique url for each created room.
     */
    public Room save(Room room) {
        updateUrl(room);
        updateModeratorPassword(room);
        return roomRepository.save(room);
    }

    /**
     * Method checks if the newly generated url exists
     * and as long as it does it is replaced by a new one.
     */
    private void updateUrl(Room room) {
        String url = null;
        while (true) {
            url = Utilities.generateRandomAlphaNumeric();
            Optional<Room> roomOpt = findByUrl(url);
            if (!roomOpt.isPresent()) {
                room.setUrl(Utilities.generateRandomAlphaNumeric());
                break;
            }
        }
    }

    /**
     * Method creates moderator password.
     */
    private void updateModeratorPassword(Room room) {
        if (room.getModeratorPassword() == null) {
            room.setModeratorPassword(Utilities.generateRandomAlphaNumeric());
        } else {
            return;
        }
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findByUrl(String url) {
        return roomRepository.findByUrl(url);
    }

    public void deleteByUrl(String url) {
        roomRepository.deleteAll(roomRepository.findAllByUrl(url));
    }

    public void close(String url) {
        roomRepository.updateOpenByUrl(false, url);
    }

    public void open(String url) {
        roomRepository.updateOpenByUrl(true, url);
    }

    /**
     * This method should be used when users request for rooms -
     * the method checks if the room is open and only if it is it is returned.
     * The check on time has to be made on the server, not to have room unavailability
     * due to Time-zone difference.
     * @param url room url
     * @return the room only if it fits the scheduled criteria
     */
    public Optional<Room> scheduledRoom(String url) {
        Optional<Room> or = roomRepository.findByUrl(url);
        if (or.isEmpty()) {
            return or;
        }
        Room r = or.get();

        if (r.getStartTime() < 0) {
            return or;
        }

        if (shouldOpen(r)) {
            if (!r.isOpen()) {
                roomRepository.updateOpenByUrl(true, url);
                r.setOpen(true);
            }
        } else {
            if (r.isOpen()) {
                roomRepository.updateOpenByUrl(false, url);
                r.setOpen(false);
            }
        }
        return Optional.ofNullable(r);
    }

    /**
     * Checks whether the room's 'open' status should be changed to true.
     *
     *
     * @param r the room
     * @return - false if the room is already open
     *         - true if it's not, and the Date of the room matches with today's date,
     *           and the current time is between the start and endtime of the room.
     */
    private boolean shouldOpen(Room r) {
        return (!r.isOpen()
                && r.getStartTime() < System.currentTimeMillis()
                && r.getEndTime() > System.currentTimeMillis());
    }

    /**
     * Edits a room.
     *
     * @param room - the updated room that will replace the old one.
     */
    public void editRoom(Room room, String oldModPassword) {
        Optional<Room> oldRoomOptional = roomRepository.findByUrl(room.getUrl());
        if (oldRoomOptional.isEmpty()) {
            return;
        }

        //Checks whether the user is authorized to edit the room
        //(and didn't manually use the edit endpoint).
        if (!oldRoomOptional.get().getModeratorPassword().equals(oldModPassword)) {
            return;
        }

        String url = room.getUrl();
        String name = room.getName();
        String modPassword = room.getModeratorPassword();
        boolean open = room.isOpen();
        long startTime = room.getStartTime();
        long endTime = room.getEndTime();
        roomRepository.editRoom(url, name, modPassword, open,
                            startTime, endTime);
    }

    /**
     * Changes the questionCooldown attribute of a Room.
     * @param room - the updated Room.
     */
    public void setCooldown(Room room, long nid) {
        if (!isAuthorized(room.getUrl(), nid)) {
            return;
        }
        String url = room.getUrl();
        int cd = room.getQuestionCooldown();
        roomRepository.setCooldown(url, cd);
    }

    /**
     * Checks whether a person is a moderator in a room.
     *
     * @param roomUrl the room url
     * @param nid     the nickname id
     * @return the whether they are authorized
     */
    private boolean isAuthorized(String roomUrl, long nid) {
        Optional<Nickname> optionalNickname = nicknameRepository.findById(nid);
        if (optionalNickname.isEmpty()) {
            return false;
        }
        Nickname nickname = optionalNickname.get();

        return (nickname.isModerator() && nickname.getRoomUrl().equals(roomUrl));
    }
}
