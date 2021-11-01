package nl.tudelft.oopp.demo.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.DebugUtils.informedErrorMessage;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import javafx.stage.Stage;

import nl.tudelft.oopp.demo.views.CreateRoomDisplay;

import org.assertj.core.api.Assertions;

import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.loadui.testfx.GuiTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import org.testfx.framework.junit.ApplicationTest;

import org.testfx.util.NodeQueryUtils;
import org.testfx.util.WaitForAsyncUtils;

class CreateRoomControllerTest extends ApplicationTest {

    private static WebDriver driver;

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(
                CreateRoomController.class.getResource("/createRoom.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(CreateRoomDisplay.class);
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /*
    @Test
    void visualRoomCreationTest_ManualModeratorPass_NoSchedule() throws TimeoutException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#roomName");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomName")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomName");
        robot.write("TestRoom");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#moderatorPassword")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#moderatorPassword");
        robot.write("TestPassword");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#scheduleOff")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#scheduleOff");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.moveTo("#createButton");
        robot.sleep(1000);
    }

    @Test
    void visualRoomCreationTest_AutoModeratorPass_NoSchedule() throws TimeoutException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#roomName");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomName")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomName");
        robot.write("TestRoom");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#generateRandomPassword")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#generateRandomPassword");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#scheduleOff")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#scheduleOff");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.moveTo("#createButton");
        robot.sleep(1000);
    }

    @Test
    void visualRoomCreationTest_AutoModeratorPass_WithSchedule() throws TimeoutException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#roomName");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomName")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomName");
        robot.write("TestRoom");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#generateRandomPassword")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#generateRandomPassword");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#scheduleOn")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#scheduleOn");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#schedule")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#schedule");
        robot.write("27/07/2021");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.moveTo("#createButton");
        robot.sleep(1000);
    }

    @Test
    void visualRoomCreationTest_NoValues() throws TimeoutException {
        FxRobot robot = new FxRobot();

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#createButton");
        robot.sleep(1000);
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#errorLabel")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        verifyThat("#errorLabel" , hasText("You have to fill in the room name!"));
    }

    @Test
    void visualRoomCreationTest_NoName() throws TimeoutException {
        FxRobot robot = new FxRobot();

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#generateRandomPassword")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#generateRandomPassword");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#createButton");
        robot.sleep(1000);
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#errorLabel")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        verifyThat("#errorLabel" , hasText("You have to fill in the room name!"));
    }

    @Test
    void visualRoomCreationTest_NoPassword() throws TimeoutException {
        FxRobot robot = new FxRobot();

        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#roomName")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#roomName");
        robot.write("TestRoom");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#createButton")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        robot.clickOn("#createButton");
        robot.sleep(1000);
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#errorLabel")
                .match(NodeQueryUtils.isVisible()).tryQuery().isPresent());
        verifyThat("#errorLabel" , hasText("You have to fill in the moderator password!"));
    }*/
}