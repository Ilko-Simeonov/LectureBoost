package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.util.Calendar;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Room;

public class JoinRoomController {

    private Stage stage; 
    
    //Container pane
    @FXML
    private Pane container;

    //Labels
    @FXML
    private Label errorLabel;

    //TextFields
    @FXML
    private TextField roomCode;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private TextField moderatorPassword;


    /**
     * Initialize on startup:
     * Sets alignment of error label to center.
     */
    public void initialize() {
        errorLabel.setAlignment(Pos.CENTER);

        EventHandler<KeyEvent> joinRoom = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    joinRoom();
                }
            }
        };
        roomCode.addEventHandler(KeyEvent.KEY_PRESSED, joinRoom);
        nicknameTextField.addEventHandler(KeyEvent.KEY_PRESSED, joinRoom);
        moderatorPassword.addEventHandler(KeyEvent.KEY_PRESSED, joinRoom);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Checks whether nickname and room url are filled in, if not, show error message,
     * If they are, query server whether a room exists for that url,
     * If it doesn't, show error message, if it does, change error message to success message.
     */
    public void joinRoom() {
        String url = roomCode.getText();
        String name = nicknameTextField.getText();
        String modPassword = moderatorPassword.getText();

        if (url.isBlank()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("You have to fill in the room url!");
            return;
        }

        if (name.isBlank()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("You have to fill in your nickname!");
            return;
        }

        Room room = ServerCommunication.getRoom(url);
        if (room == null) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("That room does not exist! :(");
            return;
        }

        Nickname nickname;

        try {
            nickname = ServerCommunication.createNickname(url, name, modPassword);
        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("That moderator password is incorrect! :(");
            return;
        }

        if (nickname == null) {
            //Moderators can join the room after it's closed
            if (!room.isOpen() && modPassword.equals("")) {
                //The server won't create a non-moderator nickname on a closed room,
                //so if nickname != null, then they are a moderator
                errorLabel.setStyle("-fx-text-fill: red;");

                if (room.getStartTime() > 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(room.getStartTime());

                    String startTime = "" + cal.get(Calendar.MINUTE);
                    
                    if (startTime.length() < 2) {
                        startTime = "0" + startTime;
                    }
                    
                    startTime = cal.get(Calendar.HOUR) + ":" + startTime;

                    if (startTime.length() < 5) {
                        startTime = "0" + startTime;
                    }

                    cal.setTimeInMillis(room.getEndTime());

                    String endTime = "" + cal.get(Calendar.MINUTE);

                    if (endTime.length() < 2) {
                        endTime = "0" + endTime;
                    }

                    endTime = cal.get(Calendar.HOUR) + ":" + endTime;

                    if (endTime.length() < 5) {
                        endTime = "0" + endTime;
                    }

                    String date = "" + cal.get(Calendar.DAY_OF_MONTH) + "-"
                            + getMonth(cal.get(Calendar.MONTH)) + "-"
                            + cal.get(Calendar.YEAR);

                    String error = "That room is scheduled on " + date
                            + " from " + startTime
                            + " to " + endTime + ".";
                    errorLabel.setText(error);
                } else {
                    errorLabel.setText("That room is closed! :(");
                }
                return;
            }

            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("That nickname is already taken! :(");
            return;
        }

        try {
            switchScene(room, nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets three letter month code by month number.
     *
     * @param month the month number
     * @return the three letter month code
     */
    private String getMonth(int month) {
        int monthCorrected = month + 1;
        switch (monthCorrected) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OKT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "ERROR";
        }
    }

    /**
     * Opens up the room.
     * @param room - the Room to open.
     * @param nickname - the nickname by which to join the room.
     * @throws IOException if the resource doesn't exist
     */
    public void switchScene(Room room, Nickname nickname) throws IOException {
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
