package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.util.Utilities;


public class CreateRoomController {

    private Stage stage;

    // Schedule-related attributes (ToggleButtons and DatePicker)
    @FXML
    private ToggleButton scheduleOn;

    @FXML
    private ToggleButton scheduleOff;

    @FXML
    private DatePicker schedule;

    @FXML
    private Spinner startHourSpinner;

    @FXML
    private Spinner startMinSpinner;

    @FXML
    private Spinner endHourSpinner;

    @FXML
    private Spinner endMinSpinner;

    @FXML
    private Pane schedulePane;

    //TextFields
    @FXML
    private TextField moderatorPassword;

    @FXML
    private TextField roomName;


    // Label
    @FXML
    private Label openRoomLabel;


    // Checkbox
    @FXML
    private CheckBox openRoomCB;


    //Error message
    @FXML
    private TextField errorLabel;

    /**
     * Adds the Enter-key shortcut for submitting.
     */
    public void initialize() {
        errorLabel.setAlignment(Pos.CENTER);

        EventHandler<KeyEvent> createRoom = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    createRoom();
                }
            }
        };
        roomName.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        moderatorPassword.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        schedule.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        startHourSpinner.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        startMinSpinner.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        endHourSpinner.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
        endMinSpinner.addEventHandler(KeyEvent.KEY_PRESSED, createRoom);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Makes the DatePicker visible.
     */
    public void showSchedule() {
        if (!scheduleOn.isSelected()) {
            scheduleOn.setSelected(true);
        }
        schedulePane.setVisible(true);
        openRoomLabel.setVisible(false);
        openRoomCB.setVisible(false);

        switchColours(scheduleOn,scheduleOff);
    }

    /**
     * Hides the DatePicker.
     */
    public void hideSchedule() {
        if (!scheduleOff.isSelected()) {
            scheduleOff.setSelected(true);
        }
        schedulePane.setVisible(false);
        openRoomLabel.setVisible(true);
        openRoomCB.setVisible(true);
        switchColours(scheduleOff,scheduleOn);
    }

    /**
     * Switches the colours and cursor-types of the ToggleButtons.
     * @param toBlue - ToggleButton that is selected (from white to blue and cursor to DEFAULT).
     * @param toWhite - the other ToggleButton (from blue to white and cursor to HAND).
     */
    private void switchColours(ToggleButton toBlue, ToggleButton toWhite) {
        toBlue.setStyle("-fx-background-color: #00a6d6");
        toBlue.setTextFill(Color.WHITE);
        toBlue.setCursor(Cursor.DEFAULT);

        toWhite.setStyle("-fx-background-color: white");
        toWhite.setTextFill(Color.valueOf("#00a6d6"));
        toWhite.setCursor(Cursor.HAND);
    }

    /**
     * Generate random moderator password through utilities class and adds it in the textField.
     */
    public void generateModeratorPassword() {
        String password = Utilities.generateRandomAlphaNumeric();
        moderatorPassword.setText(password);
    }

    /**
     * Create room with name and moderator password from textfields.
     * Shows error message under the create button if either of these is not filled in.
     * Also checks whether the room is scheduled, and if so, at what day.
     * Sends the room to the server to be added to DB,
     * and the room is returned with a randomly generated URL,
     * which is displayed under the create button.
     */
    public void createRoom() {
        String name = roomName.getText();
        String modPassword = moderatorPassword.getText();

        if (name.isBlank()) {
            errorLabel.setStyle("-fx-text-fill: red; "
                    + "-fx-background-color: transparent; "
                    + "-fx-background-insets: 0px;");
            errorLabel.setText("You have to fill in the room name!");
            return;
        }

        if (modPassword.isBlank()) {
            errorLabel.setStyle("-fx-text-fill: red; "
                    + "-fx-background-color: transparent; "
                    + "-fx-background-insets: 0px;");
            errorLabel.setText("You have to fill in the moderator password!");
            return;
        }

        boolean scheduled = scheduleOn.isSelected();
        long startTimeLong = -1;
        long endTimeLong = -1;
        if (scheduled) {
            LocalDate tempDate = schedule.getValue();
            if (tempDate == null) {
                errorLabel.setStyle("-fx-text-fill: red; "
                        + "-fx-background-color: transparent; "
                        + "-fx-background-insets: 0px;");
                errorLabel.setText("You have to pick a date!");
                return;
            }

            int startHour = (Integer) startHourSpinner.getValue();
            int startMin = (Integer) startMinSpinner.getValue();
            int endHour = (Integer) endHourSpinner.getValue();
            int endMin = (Integer) endMinSpinner.getValue();

            if (startHour > endHour
                    || ((startHour == endHour) && (startMin >= endMin))) {
                errorLabel.setStyle("-fx-text-fill: red; "
                        + "-fx-background-color: transparent; "
                        + "-fx-background-insets: 0px;");
                errorLabel.setText("Start time has to be before end time!");
                return;
            }

            LocalDateTime startTimeLdt = tempDate.atTime(startHour, startMin);
            LocalDateTime endTimeLdt = tempDate.atTime(endHour, endMin);

            Date startTime = Date.from(startTimeLdt.atZone(ZoneId.systemDefault()).toInstant());
            Date endTime = Date.from(endTimeLdt.atZone(ZoneId.systemDefault()).toInstant());

            startTimeLong = startTime.getTime();
            endTimeLong = endTime.getTime();
        }

        boolean open = openRoomCB.isSelected();

        Room room = new Room(name, open, modPassword, startTimeLong, endTimeLong);
        Room r = ServerCommunication.createRoom(room);
        String url = r.getUrl();

        if (!open) {
            errorLabel.setText("Room created with url: " + url);
            errorLabel.setStyle("-fx-text-fill: green; "
                    + "-fx-background-color: transparent; "
                    + "-fx-background-insets: 0px;");
            roomName.clear();
            moderatorPassword.clear();
            schedule.setValue(null);
            startHourSpinner.getValueFactory().setValue(0);
            startMinSpinner.getValueFactory().setValue(0);
            endHourSpinner.getValueFactory().setValue(0);
            endMinSpinner.getValueFactory().setValue(0);
            hideSchedule();
        } else {
            try {
                Nickname lecturer = ServerCommunication.createNickname(url,
                                                                "Lecturer",
                                                                      modPassword);
                switchScene(r, lecturer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens up the room.
     * @param room - the Room to open.
     * @param nickname - the nickname by which to join the room.
     * @throws IOException if the resource doesn't exist
     */
    private void switchScene(Room room, Nickname nickname) throws IOException {
        errorLabel.setText("Switching scenes...");
        errorLabel.setStyle("-fx-text-fill: green;");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/room.fxml"
                )
        );

        stage.setScene(
                new Scene(loader.load())
        );

        RoomController rc = loader.getController();
        rc.initData(room, nickname);
        rc.setStage(stage);

        stage.show();
    }

    /**
     * Goes back to the main menu.
     * @throws IOException if the reource doesn't exist
     */
    public void backToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/home.fxml"
                )
        );

        stage.setScene(
                new Scene(loader.load())
        );

        HomeController controller = (HomeController)loader.getController();
        controller.setStage(stage);

        stage.show();
    }

}
