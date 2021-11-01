package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Calls the switchScene method with joinRoom.fxml scene.
     */
    public void switchToJoinRoom() {
        try {
            switchScene("/joinRoom.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls the switchScene method with createRoom.fxml scene.
     */
    public void switchToCreateRoom() {
        try {
            switchScene("/createRoom.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls the switchScene method with chooseRoom.fxml scene.
     */
    public void switchToChooseRoom() {
        try {
            switchScene("/chooseRoom.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates and opens a new stage.
     * @param sceneName - the name of the fxml file of the scene to be opened.
     * @throws IOException if the resource doesn't exist
     */
    private void switchScene(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        sceneName
                )
        );

        stage.setScene(
                new Scene(loader.load())
        );

        if (sceneName.equals("/joinRoom.fxml")) {
            JoinRoomController controller = loader.getController();
            controller.setStage(stage);
        } else if (sceneName.equals("/createRoom.fxml")) {
            CreateRoomController controller = loader.getController();
            controller.setStage(stage);
        } else if (sceneName.equals("/chooseRoom.fxml")) {
            ChooseRoomController controller = loader.getController();
            controller.setStage(stage);
        }

        stage.show();
    }

}
