package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Room;

public class ChooseRoomController {

    private Stage stage;

    // TextFields
    @FXML
    private TextField roomCodeTextField;

    @FXML
    private TextField modPasswordTextField;

    // Label
    @FXML
    private Label errorLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Adds the Enter-key shortcut for submitting.
     */
    public void initialize() {
        EventHandler<KeyEvent> chooseRoom = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    switchToEditRoom();
                }
            }
        };

        roomCodeTextField.addEventHandler(KeyEvent.KEY_PRESSED, chooseRoom);
        modPasswordTextField.addEventHandler(KeyEvent.KEY_PRESSED, chooseRoom);
    }

    /**
     * Switches scene to the editRoom.fxml.
     */
    public void switchToEditRoom() {
        String url = roomCodeTextField.getText();
        String moderatorPassword = modPasswordTextField.getText();

        if (url.equals("")) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("You have to fill in the room url!");
            return;
        }

        if (moderatorPassword.equals("")) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("You have to fill in moderator password!");
            return;
        }

        Room room = ServerCommunication.getRoom(url);
        if (room == null) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("That room does not exist! :(");
            return;
        }

        if (!moderatorPassword.equals(room.getModeratorPassword())) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Wrong password!");
            return;
        }

        try {
            switchScene(room);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(Room room) throws IOException {
        errorLabel.setText("Switching scenes...");
        errorLabel.setStyle("-fx-text-fill: green;");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/editRoom.fxml"
                )
        );

        stage.setScene(
                new Scene(loader.load())
        );

        EditRoomController controller = loader.getController();
        controller.initData(room);
        controller.setStage(stage);

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
