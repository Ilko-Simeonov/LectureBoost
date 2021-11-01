package nl.tudelft.oopp.demo.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.views.RoomDisplay;
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



class RoomControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(RoomDisplay.class
                .getResource("/room.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(RoomDisplay.class);
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /*
    @Test
    void addQuestionVisualTest() throws TimeoutException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#questionBox");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#questionBox").match(NodeQueryUtils.isVisible()).
                tryQuery().isPresent());
        robot.clickOn("#questionBox");
        robot.write("What is a question?");
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () ->
                robot.lookup("#questionBox").match(NodeQueryUtils.isVisible()).
                tryQuery().isPresent());
        robot.moveTo("#sendButton");
    }

    @Test
    void menuNavigationTest() throws TimeoutException {
        FxRobot robot = new FxRobot();
        robot.clickOn("#openQuestionsButton");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () ->
                robot.lookup("#openQuestionsButton").match(NodeQueryUtils.
                isVisible()).tryQuery().isPresent());
        robot.clickOn("#openQuestionsButton");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () ->
                robot.lookup("#topQuestionsButton").match(NodeQueryUtils.
                isVisible()).tryQuery().isPresent());
        robot.clickOn("#topQuestionsButton");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () ->
                robot.lookup("#answeredQuestionsButton").match(NodeQueryUtils.
                isVisible()).tryQuery().isPresent());
        robot.clickOn("#answeredQuestionsButton");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () ->
                robot.lookup("#pollButton").match(NodeQueryUtils.
                        isVisible()).tryQuery().isPresent());
        robot.clickOn("#pollButton");
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () ->
                robot.lookup("#giveFeedbackButton").match(NodeQueryUtils.
                isVisible()).tryQuery().isPresent());
        robot.clickOn("#giveFeedbackButton");
    }*/
}