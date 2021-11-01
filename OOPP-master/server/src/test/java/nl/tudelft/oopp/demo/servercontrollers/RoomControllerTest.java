package nl.tudelft.oopp.demo.servercontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomControllerTest {
    private transient RoomService roomService;
    private transient RoomController roomController;
    private Room room;
    private List<Room> rooms;

    @BeforeEach
    void setup() {
        roomService = mock(RoomService.class);
        roomController = new RoomController(roomService);
        long start = 1617800400000L; //07-APR-2021 @ 15:00:00
        long end = start + 3600000; //start + 1hr
        room = new Room("Room", "url", true, "pwd", start, end, 30);
        rooms = new ArrayList<>();
    }

    @Test
    void save() {
        when(roomService.save(room)).thenReturn(room);
        assertNotNull(roomController.save(room));
    }

    @Test
    void findByUrl() {
        when(roomService.findByUrl("url2")).thenReturn(Optional.empty());
        assertNotNull(roomController.findByUrl("url2"));
    }

    @Test
    void scheduledRoom() {
        when(roomService.scheduledRoom("url2")).thenReturn(Optional.empty());
        assertNotNull(roomController.scheduledRoom("url2"));
    }

    @Test
    void findAll() {
        rooms.add(room);

        long start = 1617800400000L; //07-APR-2021 @ 15:00:00
        long end = start + 3600000; //start + 1hr
        Room r = new Room("Room2", "url2", false, "pwd", start, end, 30);

        rooms.add(r);

        List<Room> list = new ArrayList<>();
        list.add(room);
        list.add(r);

        when(roomService.findAll()).thenReturn(list);
        
        assertEquals(rooms.get(0), roomController.findAll().get(0));
        assertEquals(rooms.get(1), roomController.findAll().get(1));
    }

    @Test
    void deleteByUrl() {
        doThrow(new Successful()).when(roomService).deleteByUrl("url");

        assertThrows(Successful.class, () -> roomController.deleteByUrl("url"));
    }

    @Test
    void openRoomByUrl() {
        doThrow(new Successful()).when(roomService).open("url");

        assertThrows(Successful.class, () -> roomController.openRoomByUrl("url"));
    }

    @Test
    void closeRoomByUrl() {
        doThrow(new Successful()).when(roomService).close("url");

        assertThrows(Successful.class, () -> roomController.closeRoomByUrl("url"));
    }

    @Test
    void editRoom() {
        doThrow(new Successful()).when(roomService).editRoom(room, room.getModeratorPassword());

        assertThrows(Successful.class, () ->
                roomController.editRoom(room, room.getModeratorPassword()));
    }

    @Test
    void setCooldown() {
        doThrow(new Successful()).when(roomService).setCooldown(room, 1L);

        assertThrows(Successful.class, () -> roomController.setCooldown(room, 1));
    }
}
