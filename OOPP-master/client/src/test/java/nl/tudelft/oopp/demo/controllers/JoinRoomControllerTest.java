package nl.tudelft.oopp.demo.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.views.JoinRoomDisplay;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.NodeQueryUtils;
import org.testfx.util.WaitForAsyncUtils;

class JoinRoomControllerTest extends ApplicationTest {

    Room room = new Room("123", "123", true, "Test", 1, 2);

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(CreateRoomController.class.getResource("/joinRoom.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(JoinRoomDisplay.class);
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /*
    @Test
    void visualRoomJoiningTest_NoModerator() throws TimeoutException {
        FxRobot robot = new FxRobot();


        ServerCommunication.createRoom(r);

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomCode")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomCode");
        robot.write(r.getUrl());
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#nicknameTextField")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#nicknameTextField");
        robot.write("TestDummy");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#joinButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#joinButton");
        robot.sleep(2000);

    }

    @Test
    void visualRoomJoiningTest_NoValues() throws TimeoutException {
        FxRobot robot = new FxRobot();
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#joinButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#joinButton");
        robot.sleep(2000);
    }

    @Test
    void visualRoomJoiningTest_OnlyUrl() throws TimeoutException {
        FxRobot robot = new FxRobot();
        ServerCommunication.createRoom(r);

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomCode")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomCode");
        robot.write(r.getUrl());
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#joinButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#joinButton");
        robot.sleep(2000);
    }

    @Test
    void visualRoomJoiningTest_OnlyNickName() throws TimeoutException {
        FxRobot robot = new FxRobot();
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#nicknameTextField")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#nicknameTextField");
        robot.write("TestDummy2");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#joinButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#joinButton");
        robot.sleep(2000);
    }*/
}