package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.util.Utilities;

public class EditRoomController {

    private Stage stage;

    // Room that's being editted
    private Room room;


    // TextFields
    @FXML
    private TextField roomName;

    @FXML
    private TextField moderatorPassword;


    // ToggleButtons
    @FXML
    private ToggleButton scheduleOn;

    @FXML
    private ToggleButton scheduleOff;

    // Schedule related elements
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


    // Label
    @FXML
    private Label openRoomLabel;


    // CheckBox
    @FXML
    private CheckBox openRoomCB;


    // Error message
    @FXML
    private TextField errorLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes data and the content of the GUI elements.
     * @param room - the Room that's edited.
     */
    public void initData(Room room) {
        this.room = room;

        roomName.setText(room.getName());
        moderatorPassword.setText(room.getModeratorPassword());
        if (room.getStartTime() >= 0) {
            setSchedule(room);
            showSchedule();
        }
        if (room.isOpen()) {
            openRoomCB.setSelected(true);
        }
    }

    /**
     * Adds the Enter-key shortcut for submitting.
     */
    public void initialize() {
        EventHandler<KeyEvent> editRoom = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    editRoom();
                }
            }
        };
        roomName.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        moderatorPassword.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        schedule.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        startHourSpinner.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        startMinSpinner.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        endHourSpinner.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
        endMinSpinner.addEventHandler(KeyEvent.KEY_PRESSED, editRoom);
    }

    private void setSchedule(Room room) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(room.getStartTime());
        startHourSpinner.getValueFactory().setValue(cal.get(Calendar.HOUR_OF_DAY));
        startMinSpinner.getValueFactory().setValue(cal.get(Calendar.MINUTE));

        cal.setTimeInMillis(room.getEndTime());
        endHourSpinner.getValueFactory().setValue(cal.get(Calendar.HOUR_OF_DAY));
        endMinSpinner.getValueFactory().setValue(cal.get(Calendar.MINUTE));

        LocalDate ld = LocalDate.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        schedule.setValue(ld);
    }

    /**
     * Edits the room's attributes based on the content of the GUI elements.
     */
    public void editRoom() {
        String name = roomName.getText();
        String modPassword = moderatorPassword.getText();

        if (name.equals("")) {
            errorLabel.setStyle("-fx-text-fill: red; "
                    + "-fx-background-color: transparent; "
                    + "-fx-background-insets: 0px;");
            errorLabel.setText("You have to fill in the room name!");
            return;
        }

        if (modPassword.equals("")) {
            errorLabel.setStyle("-fx-text-fill: red; "
                    + "-fx-background-color: transparent; "
                    + "-fx-background-insets: 0px;");
            errorLabel.setText("You have to fill in the moderator password!");
            return;
        }


        boolean open = openRoomCB.isSelected();
        room.setOpen(open);

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
            }

            LocalDateTime startTimeLdt = tempDate.atTime(startHour, startMin);
            LocalDateTime endTimeLdt = tempDate.atTime(endHour, endMin);

            java.util.Date startTime = java.util.Date.from(
                    startTimeLdt.atZone(ZoneId.systemDefault()).toInstant());
            java.util.Date endTime = java.util.Date.from(
                    endTimeLdt.atZone(ZoneId.systemDefault()).toInstant());

            startTimeLong = startTime.getTime();
            endTimeLong = endTime.getTime();
        }
        room.setStartTime(startTimeLong);
        room.setEndTime(endTimeLong);

        if (room.getStartTime() < 0) {
            schedule.setValue(null);
            startHourSpinner.getValueFactory().setValue(0);
            startMinSpinner.getValueFactory().setValue(0);
            endHourSpinner.getValueFactory().setValue(0);
            endMinSpinner.getValueFactory().setValue(0);
        }

        String oldModPassword = room.getModeratorPassword();
        room.setName(name);
        room.setModeratorPassword(modPassword);
        ServerCommunication.editRoom(room, oldModPassword);

        errorLabel.setStyle("-fx-text-fill: green; "
                + "-fx-background-color: transparent; "
                + "-fx-background-insets: 0px;");
        errorLabel.setText("The changes have been made to room \"" + name + "\"!");
    }

    /**
     * Generate random moderator password through utilities class and adds it in the textField.
     */
    public void generateModeratorPassword() {
        String password = Utilities.generateRandomAlphaNumeric();
        moderatorPassword.setText(password);
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
        toBlue.setTextFill(javafx.scene.paint.Color.WHITE);
        toBlue.setCursor(javafx.scene.Cursor.DEFAULT);

        toWhite.setStyle("-fx-background-color: white");
        toWhite.setTextFill(Color.valueOf("#00a6d6"));
        toWhite.setCursor(Cursor.HAND);
    }

    /**
     * Goes back to the main menu.
     * @throws IOException if the resource doesn't exist
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
