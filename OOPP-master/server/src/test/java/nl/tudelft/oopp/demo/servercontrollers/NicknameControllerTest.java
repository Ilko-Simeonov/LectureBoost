package nl.tudelft.oopp.demo.servercontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.errors.Successful;
import nl.tudelft.oopp.demo.services.NicknameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NicknameControllerTest {
    private transient NicknameController nicknameController;
    private transient NicknameService nicknameService;
    private Nickname nickname;
    private List<Nickname> names;

    @BeforeEach
    void setup() {
        nicknameService = Mockito.mock(NicknameService.class);
        nicknameController = new NicknameController(nicknameService);
        nickname = new Nickname("Jeff", "url", false);
        names = new ArrayList<>();
    }

    @Test
    void save() {
        Nickname mock = new Nickname("Jeff", "url", false);
        when(nicknameService.save(nickname,"")).thenReturn(mock);
        assertEquals(nickname.getName(), nicknameController.save(nickname, "").getName());
        assertEquals(nickname.getRoomUrl(), nicknameController.save(nickname, "").getRoomUrl());
        assertFalse(nickname.isModerator() && mock.isModerator());
    }

    @Test
    void findAll() {
        names.add(new Nickname("Jeff", "url", false));
        names.add(new Nickname("Jeff", "url", true));
        when(nicknameService.findAll()).thenReturn(names);
        assertEquals(nickname.getName(), nicknameController.findAll().get(0).getName());
        assertEquals(nickname.getRoomUrl(), nicknameController.findAll().get(0).getRoomUrl());
        assertFalse(nickname.isModerator() && nicknameController.findAll().get(0).isModerator());
        assertFalse(nicknameController.findAll().get(1).isModerator() == nickname.isModerator());
    }

    @Test
    void findById() {
        names.add(nickname);
        when(nicknameService.findById(1L)).thenReturn(Optional.empty());
        assertFalse(nicknameController.findById(1L).isPresent());
    }

    @Test
    void findByUrl() {
        names.add(nickname);
        names.add(new Nickname("A", "url2", false));
        names.add(new Nickname("B", "url3", true));
        names.add(new Nickname("C", "url3", false));
        List<Nickname> newNames = new ArrayList<>();
        newNames.add(new Nickname("B", "url3", true));
        newNames.add(new Nickname("C", "url3", true));
        when(nicknameService.findByUrl("url3")).thenReturn(newNames);
        assertEquals(newNames, nicknameController.findByUrl("url3"));
    }

    @Test
    void findByUrlAndName() {
        names.add(nickname);
        when(nicknameService.findByUrlAndName("url3", "Jeff")).thenReturn(Optional.empty());
        assertEquals(nicknameController.findByUrlAndName("url3", "Jeff"), Optional.empty());
    }

    @Test
    void findModeratorsByUrl() {
        names.add(nickname);
        when(nicknameController.findModeratorsByUrl("url")).thenReturn(null);
        assertNull(nicknameController.findModeratorsByUrl("url"));
    }

    @Test
    void leaveById() {
        doThrow(new Successful()).when(nicknameService).leaveById(1L);

        assertThrows(Successful.class, () -> nicknameController.leaveById(1L));
    }

    @Test
    void amountOfParticipants() {
        when(nicknameService.amountOfParticipants("url")).thenReturn(12);

        assertEquals(12, nicknameController.amountOfParticipants("url"));
    }

    @Test
    void change() {
        doThrow(new Successful()).when(nicknameService).change(nickname);

        assertThrows(Successful.class, () -> nicknameController.change(nickname));
    }
}
