package nl.tudelft.oopp.demo.servercontrollers;

import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.services.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    Room save(@RequestBody Room room) {
        return roomService.save(room);
    }

    @GetMapping("/{room}/get")
    public Optional<Room> findByUrl(@PathVariable String room) {
        return roomService.findByUrl(room);
    }

    @GetMapping("/{room}/scheduled")
    public Optional<Room> scheduledRoom(@PathVariable String room) {
        return roomService.scheduledRoom(room);
    }

    @GetMapping("/get")
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @DeleteMapping("/delete/{url}")
    public void deleteByUrl(@PathVariable String url) {
        roomService.deleteByUrl(url);
    }

    @GetMapping("/open/{url}")
    public void openRoomByUrl(@PathVariable String url) {
        roomService.open(url);
    }

    @GetMapping("/close/{url}")
    public void closeRoomByUrl(@PathVariable String url) {
        roomService.close(url);
    }

    @PutMapping("/edit")
    public void editRoom(@RequestBody Room room, @RequestParam String p) {
        roomService.editRoom(room, p);
    }

    @PutMapping("/set_cooldown")
    public void setCooldown(@RequestBody Room room, @RequestParam long n) {
        roomService.setCooldown(room, n);
    }
}
