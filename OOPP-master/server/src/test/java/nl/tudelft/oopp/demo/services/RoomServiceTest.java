package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

public class RoomServiceTest {
    private transient RoomService roomService;
    private transient RoomRepository roomRepository;
    private transient NicknameRepository nicknameRepository;

    private Room room;
    private Room room2;
    private List<Room> rooms;

    @BeforeEach
    void setup() {
        roomRepository = mock(RoomRepository.class);
        nicknameRepository = mock(NicknameRepository.class);

        roomService = new RoomService(roomRepository, nicknameRepository);

        long start = 1617800400000L; //07-APR-2021 @ 15:00:00
        long end = start + 3600000; //start + 1hr
        room = new Room("Room", "url", true, "pwd", start, end, 30);
        room2 = new Room("name2", "url2", false, "pwd2", start, end, 30);

        rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room2);
    }

    @Test
    void save1() {
        //Copy of room
        Room oldRoom = new Room(
                room.getName(),
                room.getUrl(),
                room.isOpen(),
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.save(any(Room.class))).thenAnswer(answer -> answer.getArguments()[0]);

        Room savedRoom = roomService.save(room);
        assertNotEquals(oldRoom.getUrl(), savedRoom.getUrl());

        savedRoom.setUrl(oldRoom.getUrl());

        assertEquals(oldRoom, savedRoom);
    }

    @Test
    void save2() {
        room.setModeratorPassword(null);

        //Copy of room
        Room oldRoom = new Room(
                room.getName(),
                room.getUrl(),
                room.isOpen(),
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.save(any(Room.class))).thenAnswer(answer -> answer.getArguments()[0]);

        Room savedRoom = roomService.save(room);
        assertNotEquals(oldRoom.getUrl(), savedRoom.getUrl());
        assertNotEquals(savedRoom.getModeratorPassword(), oldRoom.getModeratorPassword());

        savedRoom.setUrl(oldRoom.getUrl());
        savedRoom.setModeratorPassword(oldRoom.getModeratorPassword());

        assertEquals(oldRoom, savedRoom);
    }

    @Test
    void saveNull() {
        assertThrows(NullPointerException.class, () -> roomService.save(null));
    }

    @Test
    void findAll() {
        when(roomRepository.findAll()).thenReturn(rooms);
        assertEquals(rooms, roomService.findAll());
    }

    @Test
    void findByUrl() {
        when(roomRepository.findByUrl("url")).thenReturn(Optional.ofNullable(room));
        assertEquals(Optional.ofNullable(room), roomService.findByUrl("url"));
    }

    @Test
    void findByUrlNull() {
        when(roomRepository.findByUrl("urlNull")).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), roomService.findByUrl("urlNull"));
    }

    @Test
    void deleteByUrl() {
        List<Room> delRooms = new ArrayList<>();
        delRooms.add(room);

        when(roomRepository.findAllByUrl("url")).thenReturn(delRooms);
        doThrow(new Successful()).when(roomRepository).deleteAll(delRooms);

        assertThrows(Successful.class, () -> roomService.deleteByUrl("url"));
    }

    @Test
    void open() {
        doThrow(new Successful()).when(roomRepository).updateOpenByUrl(true, "url");

        assertThrows(Successful.class, () -> roomService.open("url"));
    }

    @Test
    void close() {
        doThrow(new Successful()).when(roomRepository).updateOpenByUrl(false, "url");

        assertThrows(Successful.class, () -> roomService.close("url"));
    }

    @Test
    //This test will fail if it is executed on April 7th 2021 between 15:00 and 16:00
    void scheduledRoomClosed() {
        room.setOpen(false);
        Room roomCopy = new Room(
                room.getName(),
                room.getUrl(),
                room.isOpen(),
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertEquals(Optional.of(roomCopy), roomService.scheduledRoom(room.getUrl()));
    }

    @Test
    //This test will fail if it is executed on April 7th 2021 between 15:00 and 16:00
    void scheduledRoomShouldClose() {
        room.setOpen(true);
        Room roomCopy = new Room(
                room.getName(),
                room.getUrl(),
                false,
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertEquals(Optional.of(roomCopy), roomService.scheduledRoom(room.getUrl()));
    }

    @Test
    //This test will fail if it takes longer than 10 hours to execute.
    void scheduledRoomShouldOpen() {
        room.setOpen(false);
        room.setStartTime(System.currentTimeMillis() - 3600000);
        room.setEndTime(System.currentTimeMillis() + 36000000);
        Room roomCopy = new Room(
                room.getName(),
                room.getUrl(),
                true,
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertEquals(Optional.of(roomCopy), roomService.scheduledRoom(room.getUrl()));
    }

    @Test
    void nonScheduledRoom() {
        room.setOpen(false);
        room.setStartTime(-1);
        room.setEndTime(-1);
        Room roomCopy = new Room(
                room.getName(),
                room.getUrl(),
                room.isOpen(),
                room.getModeratorPassword(),
                room.getStartTime(),
                room.getEndTime(),
                room.getQuestionCooldown()
        );

        when(roomRepository.findByUrl("url")).thenReturn(Optional.of(room));

        assertEquals(Optional.of(roomCopy), roomService.scheduledRoom(room.getUrl()));

        room.setOpen(true);
        roomCopy.setOpen(true);

        assertEquals(Optional.of(roomCopy), roomService.scheduledRoom(room.getUrl()));
    }

    @Test
    void scheduledRoomDne() {
        when(roomRepository.findByUrl("urlDne")).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), roomService.scheduledRoom(room.getUrl()));
    }

    @Test
    void editRoom() {
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.of(room));

        doThrow(new Successful()).when(roomRepository).editRoom(
                room.getUrl(),
                room.getName(),
                room.getModeratorPassword(),
                room.isOpen(),
                room.getStartTime(),
                room.getEndTime()
        );

        assertThrows(Successful.class, () ->
                roomService.editRoom(room, room.getModeratorPassword()));
    }

    @Test
    void editRoomDne() {
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.empty());

        doThrow(new Successful()).when(roomRepository).editRoom(
                room.getUrl(),
                room.getName(),
                room.getModeratorPassword(),
                room.isOpen(),
                room.getStartTime(),
                room.getEndTime()
        );

        try {
            roomService.editRoom(room, room.getModeratorPassword());
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void editRoomWrongModPwd() {
        when(roomRepository.findByUrl(room.getUrl())).thenReturn(Optional.of(room));

        doThrow(new Successful()).when(roomRepository).editRoom(
                room.getUrl(),
                room.getName(),
                room.getModeratorPassword(),
                room.isOpen(),
                room.getStartTime(),
                room.getEndTime()
        );

        try {
            roomService.editRoom(room, "wrongPWD");
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void setCooldown() {
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("Mod", room.getUrl(), true)
        ));

        doThrow(new Successful()).when(roomRepository)
                .setCooldown(room.getUrl(), room.getQuestionCooldown());

        assertThrows(Successful.class, () -> roomService.setCooldown(room, 2));
    }

    @Test
    void setCooldownNicknameDne() {
        when(nicknameRepository.findById(2L)).thenReturn(Optional.empty());

        doThrow(new Successful()).when(roomRepository)
                .setCooldown(room.getUrl(), room.getQuestionCooldown());

        try {
            roomService.setCooldown(room, 2L);
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void setCooldownNicknameNotModerator() {
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("Mod", room.getUrl(), false)
        ));

        doThrow(new Successful()).when(roomRepository)
                .setCooldown(room.getUrl(), room.getQuestionCooldown());

        try {
            roomService.setCooldown(room, 2L);
        } catch (Successful s) {
            fail();
        }
    }

    @Test
    void setCooldownNicknameOtherRoom() {
        when(nicknameRepository.findById(2L)).thenReturn(Optional.of(
                new Nickname("Mod", "wrongUrl", true)
        ));

        doThrow(new Successful()).when(roomRepository)
                .setCooldown(room.getUrl(), room.getQuestionCooldown());

        try {
            roomService.setCooldown(room, 2L);
        } catch (Successful s) {
            fail();
        }
    }


}