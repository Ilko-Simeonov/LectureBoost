package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.repositories.NicknameRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.services.NicknameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NicknameServiceTest {
    private transient NicknameService nicknameService;
    private transient NicknameRepository nicknameRepository;
    private transient RoomRepository roomRepository;
    private Nickname nickname;
    private Room room;
    private List<Nickname> names;
    List<Nickname> newNames;

    @BeforeEach
    void setup() {
        nicknameRepository = mock(NicknameRepository.class);
        roomRepository = mock(RoomRepository.class);

        nicknameService = new NicknameService(nicknameRepository,roomRepository);

        nickname = new Nickname("name", "url", false);

        names = new ArrayList<>();
        names.add(nickname);

        newNames = new ArrayList<>();

        long start = 1617800400000L; //07-APR-2021 @ 15:00:00
        long end = start + 3600000; //start + 1hr
        room = new Room("Room", "url", true, "pwd", start, end, 30);
    }

    @Test
    void findByUrl() {
        names.add(new Nickname("B", "url3", true));
        names.add(new Nickname("C", "url3", false));
        newNames.add(new Nickname("B", "url3", true));
        newNames.add(new Nickname("C", "url3", true));
        when(nicknameService.findByUrl("url3")).thenReturn(newNames);
        String url = nicknameService.findByUrl("url3").get(0).getRoomUrl();
        assertEquals(names.get(1).getName(), nicknameService.findByUrl("url3").get(0).getName());
        assertEquals(names.get(1).getRoomUrl(),url);
    }

    @Test
    void saveNonModerator() {
        nickname.setModerator(true);
        nickname.setActive(false);

        Nickname expectedNickname = new Nickname(
                nickname.getName(),
                nickname.getRoomUrl(),
                false
        );
        expectedNickname.setActive(true);

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));
        when(nicknameRepository.findByUrlAndName(
                nickname.getRoomUrl(),
                nickname.getName()
        )).thenReturn(Optional.empty());

        when(nicknameRepository.save(any(Nickname.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(expectedNickname, nicknameService.save(nickname, ""));
    }

    @Test
    void saveModerator() {
        nickname.setModerator(false);
        nickname.setActive(false);

        Nickname expectedNickname = new Nickname(
                nickname.getName(),
                nickname.getRoomUrl(),
                true
        );
        expectedNickname.setActive(true);

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));
        when(nicknameRepository.findByUrlAndName(
                nickname.getRoomUrl(),
                nickname.getName()
        )).thenReturn(Optional.empty());

        when(nicknameRepository.save(any(Nickname.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(expectedNickname, nicknameService.save(nickname, "pwd"));
    }

    @Test
    void saveRoomDne() {
        when(roomRepository.findByUrl("Dne")).thenReturn(Optional.empty());

        nickname.setRoomUrl("Dne");

        assertThrows(ResponseStatusException.class, () -> nicknameService.save(nickname, "pwd"));
    }

    @Test
    void saveClosedRoom() {
        room.setOpen(false);
        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertNull(nicknameService.save(nickname, ""));
    }

    @Test
    void saveModeratorClosedRoom() {
        nickname.setModerator(false);
        nickname.setActive(false);
        room.setOpen(false);

        Nickname expectedNickname = new Nickname(
                nickname.getName(),
                nickname.getRoomUrl(),
                true
        );
        expectedNickname.setActive(true);

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));
        when(nicknameRepository.findByUrlAndName(
                nickname.getRoomUrl(),
                nickname.getName()
        )).thenReturn(Optional.empty());

        when(nicknameRepository.save(any(Nickname.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        assertEquals(expectedNickname, nicknameService.save(nickname, "pwd"));
    }

    @Test
    void saveWrongModeratorPassword() {
        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertThrows(ResponseStatusException.class, () -> nicknameService.save(nickname, "wrong"));
    }

    @Test
    void saveNicknameAlreadyExists() {
        nickname.setModerator(true);
        nickname.setActive(false);

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));
        when(nicknameRepository.findByUrlAndName(
                nickname.getRoomUrl(),
                nickname.getName()
        )).thenReturn(Optional.of(new Nickname(nickname.getName(), nickname.getRoomUrl(), false)));

        assertNull(nicknameService.save(nickname, ""));
        assertNull(nicknameService.save(nickname, "pwd"));
        assertThrows(ResponseStatusException.class, () -> nicknameService.save(nickname, "wrong"));
    }


    @Test
    void findAll() {
        newNames.add(new Nickname("name", "url", false));
        when(nicknameRepository.findAll()).thenReturn(names);
        assertEquals(newNames.get(0).getName(), nicknameService.findAll().get(0).getName());
        assertEquals(newNames.get(0).getRoomUrl(), nicknameService.findAll().get(0).getRoomUrl());
    }

    @Test
    void findById() {
        when(nicknameRepository.findById(123L)).thenReturn(Optional.empty());
        assertTrue(nicknameService.findById(123L).isEmpty());
    }

    @Test
    void findModeratorsByUrl() {
        newNames.add(new Nickname("abc", "url", false));
        List<Nickname> n = new ArrayList<>();
        when(nicknameRepository.findModeratorsByUrl(newNames.get(0).getRoomUrl())).thenReturn(n);
        assertTrue(nicknameService.findModeratorsByUrl(newNames.get(0).getRoomUrl()).isEmpty());
    }

    @Test
    void findByUrlAndName() {
        newNames.add(new Nickname("name", "url", true));
        Optional<Nickname> name = Optional.ofNullable(newNames.get(0));
        when(nicknameRepository.findByUrlAndName("url", "name")).thenReturn(name);
        assertFalse(nicknameService.findByUrlAndName("url", "name").isEmpty());
    }

    @Test
    void leaveById() {
        doThrow(new Successful()).when(nicknameRepository).leaveById(1L, false);

        assertThrows(Successful.class, () -> nicknameService.leaveById(1));
    }

    @Test
    void amountOfParticipants() {
        when(nicknameRepository.amountOfParticipants("url")).thenReturn(217);

        assertEquals(217, nicknameService.amountOfParticipants("url"));
    }

    @Test
    void change() {
        doThrow(new Successful()).when(nicknameRepository)
                .change(nickname.getId(), nickname.getName());

        assertThrows(Successful.class, () -> nicknameService.change(nickname));
    }
}
