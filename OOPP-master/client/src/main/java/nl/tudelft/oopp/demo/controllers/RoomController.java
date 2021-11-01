package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Feedback;
import nl.tudelft.oopp.demo.entities.FeedbackType;
import nl.tudelft.oopp.demo.entities.Nickname;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;

public class RoomController {

    private Stage stage;

    // Room data
    private Room room;
    private Nickname nickname;

    // Labels
    @FXML
    private Label roomNameLabel;

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label peopleCount;

    @FXML
    private Label questionCooldownLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label emptyQuestion;


    // Left-side menu ToggleButtons
    @FXML
    private ToggleButton openQuestionsButton;

    @FXML
    private ToggleButton topQuestionsButton;

    @FXML
    private ToggleButton answeredQuestionsButton;

    @FXML
    private ToggleButton giveFeedbackButton;

    @FXML
    private ToggleButton pollButton;
    
    @FXML
    private ToggleButton slowModeOff;

    @FXML
    private ToggleButton slowModeOn;


    // Tabs
    @FXML
    private Pane openQuestionsTab;

    @FXML
    private Pane topQuestionsTab;

    @FXML
    private Pane answeredQuestionsTab;

    @FXML
    private Pane giveFeedbackTab;

    @FXML
    private Pane pollTab;

    @FXML
    private AnchorPane helpTab;

    @FXML
    private AnchorPane settingsTab;


    // VBox
    @FXML
    private VBox questions;

    @FXML
    private VBox presenterMode;

    @FXML
    private VBox polls;

    @FXML
    private VBox pollResults;

    @FXML
    private VBox answeredQuestions;


    // TextField
    @FXML
    private TextArea questionBox;

    @FXML
    private TextField roomCodeLabel;

    @FXML
    private TextField changeNickname;

    @FXML
    private TextField topQuestionsAmountTF;

    @FXML
    private TextField questionCooldownTF;

    @FXML
    private TextField feedbackResetTF;


    //ImageViews

    @FXML
    private ImageView createPollButton;

    @FXML
    private ImageView downloadCsvButton;


    // Send button
    @FXML
    private ImageView sendButton;

    private PieChart storedPieChart;


    // Necessary for cooldown timer
    private Timeline timeline;
    private Integer cooldown = 30;
    private Integer commonCooldownPeriod = 0;

    private static final Integer GIVE_FEEDBACK_COOL_DOWN_PERIOD = 30;

    Timeline closeError;

    private Integer topQuestionsAmount = 5;

    /**
     * Initializes data.
     *
     * @param room     sets the room
     * @param nickname sets the nickname
     */
    public void initData(Room room, Nickname nickname) {
        this.room = room;
        this.nickname = nickname;

        roomNameLabel.setText(this.room.getName());
        nicknameLabel.setText(nickname.getName());
        roomCodeLabel.setText(this.room.getUrl());
        changeNickname.setText(this.nickname.getName());
        topQuestionsAmountTF.setText(String.valueOf(topQuestionsAmount));


        if (this.room.getQuestionCooldown() > 0) {
            showCooldown();
        } else {
            hideCooldown();
        }

        int cd = room.getQuestionCooldown();
        questionCooldownTF.setText(String.valueOf(cd));

        if (!nickname.isModerator()) {
            slowModeOn.setDisable(true);
            slowModeOff.setDisable(true);
            questionCooldownTF.setDisable(true);
            feedbackResetTF.setDisable(true);
        }
        closeHelp();

        settingsTab.toBack();
        errorLabel.setVisible(false);

        commonCooldownPeriod = 0;

        getAllQuestions();
        initializeFeedBackScreen();
        getTopQuestions();
    }

    private void refreshRoom() {
        this.room = ServerCommunication.getRoom(this.room.getUrl());

        if (!room.isOpen()) {
            try {
                backToMainMenu();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        roomNameLabel.setText(this.room.getName());

        if (this.room.getQuestionCooldown() > 0) {
            showCooldown();
        } else {
            hideCooldown();
        }

        int cd = room.getQuestionCooldown();
        questionCooldownTF.setText(String.valueOf(cd));
    }

    private void refreshNickname() {
        this.nickname = ServerCommunication.getNickname(this.nickname.getId());
        if (nickname.isMuted()) {
            cooldown = 0;
            questionBox.setDisable(true);
            sendButton.setVisible(false);
            timerLabel.setVisible(true);

            timerLabel.setText("You are muted!");
        }
        nicknameLabel.setText(nickname.getName());
        changeNickname.setText(this.nickname.getName());
    }

    /**
     * Shows all questions.
     * Creates a Timeline to make the emptyQuestion Label invisible after 3 seconds.
     * Adds the Enter-key shortcut for submitting a question.
     */
    public void initialize() {
        showOpenQuestions();

        closeError = new Timeline();
        closeError.setCycleCount(1);
        closeError.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        emptyQuestion.setVisible(false);
                    }
                }));
                
        questionBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addQuestion();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Brings the open questions tab to the front.
     */
    public void showOpenQuestions() {
        openQuestionsTab.toFront();
        changeColour(openQuestionsButton);
    }

    /**
     * Brings the top questions tab to the front.
     */
    public void showTopQuestions() {
        topQuestionsTab.toFront();
        changeColour(topQuestionsButton);
    }

    /**
     * Brings the answered questions tab to the front.
     */
    public void showAnsweredQuestions() {
        answeredQuestionsTab.toFront();
        changeColour(answeredQuestionsButton);
    }

    /**
     * Brings the feedback tab to the front.
     */
    public void showFeedbackOptions() {
        giveFeedbackTab.toFront();
        changeColour(giveFeedbackButton);
        initializeFeedBackScreen();
    }

    /**
     * Brings the poll tab to the front.
     */
    public void showPolls() {
        pollTab.toFront();
        changeColour(pollButton);
    }

    /**
     * Makes the ToggleButton corresponding to the open tab the same color and others darker.
     * Also changes cursor types.
     * @param selected - ToggleButton corresponding to the open tab.
     */
    private void changeColour(ToggleButton selected) {
        List<ToggleButton> buttons = Arrays.asList(
                openQuestionsButton,
                topQuestionsButton,
                answeredQuestionsButton,
                giveFeedbackButton,
                pollButton
        );
        downloadCsvButton.setVisible(selected.equals(answeredQuestionsButton));
        for (ToggleButton button : buttons) {
            if (button.equals(selected)) {
                button.setStyle("-fx-background-color: #62c6e2");
                button.setCursor(Cursor.DEFAULT);
            } else {
                button.setStyle("-fx-background-color: #00a6d6");
                button.setCursor(Cursor.HAND);
            }
        }
    }

    // The next 10 methods are for changing the ToggleButton colour while hovering
    //The next 8 methods call hoverStart or hoverEnd to the hovered ToggleButton.
    //They only do it if the button is not selected.
    /**
     * Cursor enters openQuestionButton.
     */
    public void hoverOpenStart() {
        if (openQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverStart(openQuestionsButton);
        }
    }

    /**
     * Cursor exits openQuestionsButton.
     */
    public void hoverOpenEnd() {
        if (openQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverEnd(openQuestionsButton);
        }
    }

    /**
     * Cursor enters topQuestionButton.
     */
    public void hoverTopStart() {
        if (topQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverStart(topQuestionsButton);
        }
    }

    /**
     * Cursor exits topQuestionsButton.
     */
    public void hoverTopEnd() {
        if (topQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverEnd(topQuestionsButton);
        }
    }

    /**
     * Cursor enters answeredQuestionButton.
     */
    public void hoverAnsweredStart() {
        if (answeredQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverStart(answeredQuestionsButton);
        }
    }

    /**
     * Cursor exits answeredQuestionsButton.
     */
    public void hoverAnsweredEnd() {
        if (answeredQuestionsButton.getCursor().equals(Cursor.HAND)) {
            hoverEnd(answeredQuestionsButton);
        }
    }

    /**
     * Cursor enters giveFeedbackButton.
     */
    public void hoverFeedbackStart() {
        if (giveFeedbackButton.getCursor().equals(Cursor.HAND)) {
            hoverStart(giveFeedbackButton);
        }
    }

    /**
     * Cursor exits giveFeedbackButton.
     */
    public void hoverFeedbackEnd() {
        if (giveFeedbackButton.getCursor().equals(Cursor.HAND)) {
            hoverEnd(giveFeedbackButton);
        }
    }

    /**
     * Cursor enters pollButton.
     */
    public void hoverPollsStart() {
        if (pollButton.getCursor().equals(Cursor.HAND)) {
            hoverStart(pollButton);
        }
    }

    /**
     * Cursor exits pollButton.
     */
    public void hoverPollsEnd() {
        if (pollButton.getCursor().equals(Cursor.HAND)) {
            hoverEnd(pollButton);
        }
    }

    /**
     * Sets the colour of a ToggleButton to a darker shade.
     * @param button - the ToggleButton which is being hovered.
     */
    private void hoverStart(ToggleButton button) {
        button.setStyle("-fx-background-color: #0087e0");
    }

    /**
     * Sets the colour of a ToggleButton to a lighter shade.
     * @param button - the ToggleButton which was hovered.
     */
    private void hoverEnd(ToggleButton button) {
        button.setStyle("-fx-background-color: #00a6d6");
    }

    /**
     * Opens up main menu.
     * @throws IOException if the resource doesn't exist.
     */
    public void backToMainMenu() throws IOException {
        ServerCommunication.leaveRoom(this.nickname.getId());
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

    /**
     * Adds the asked question to the server and all questions to the screen.
     */
    public void addQuestion() {
        String question = questionBox.getText() + System.lineSeparator();
        if (question.isBlank()) {
            questionBox.setStyle("-fx-prompt-text-fill: red;");
            questionBox.setPromptText("You have to fill in a question!");
            return;
        }
        questionBox.setStyle("-fx-prompt-text-fill: grey;");
        questionBox.setPromptText("ASK YOUR QUESTION");

        String url = this.room.getUrl();
        long nid = nickname.getId();
        ServerCommunication.createQuestion(url, question, nid);
        getAllQuestions();
        questionBox.clear();
    }

    /**
     * Disables the question box for {cooldown} seconds and sets a timer on it.
     */
    private void setCooldown() {
        this.cooldown = this.room.getQuestionCooldown();
        if (this.cooldown <= 0) {
            return;
        }

        questionBox.setDisable(true);
        sendButton.setVisible(false);
        timerLabel.setVisible(true);

        timerLabel.setText(cooldown.toString());

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (nickname.isMuted()) {
                            timeline.stop();
                            return;
                        }
                        cooldown--;
                        timerLabel.setText(cooldown.toString());
                        if (cooldown <= 0) {
                            timeline.stop();
                            questionBox.setDisable(false);
                            sendButton.setVisible(true);
                            timerLabel.setVisible(false);
                        }
                    }
                }));
        timeline.playFromStart();
    }

    /**
     * Disables the feedback buttons for {cooldown} seconds and sets a timer on it.
     */
    private void setCooldown(List<Node> uiElements, Label timerLabel, Integer timeInSeconds) {
        this.commonCooldownPeriod = timeInSeconds;
        for (Node node : uiElements) {
            node.setDisable(true);
        }
        timerLabel.setText("Please wait for "
                + commonCooldownPeriod.toString() + " seconds...");
        timerLabel.setVisible(true);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        commonCooldownPeriod--;
                        timerLabel.setText("Please wait for "
                                + commonCooldownPeriod.toString() + " seconds...");
                        if (commonCooldownPeriod == 0) {
                            timeline.stop();
                            timerLabel.setVisible(false);
                            for (Node node : uiElements) {
                                node.setDisable(false);
                            }
                        }
                    }
                }
        ));
        timeline.playFromStart();
    }

    public void setParticipants() {
        int participants = ServerCommunication.getAmountOfParticipants(this.room.getUrl());
        peopleCount.setText(Integer.toString(participants));
    }

    /**
     * Creates the layout of the a question and adds it onto the screen.
     * @param q - the Question which's layout is to be created.
     * @param tab - the Vbox into which the layout is created.
     */
    private void createLayout(Question q, VBox tab) {
        Nickname nn = ServerCommunication.getNickname(q.getNicknameId());

        Pane questionPane = createPane(tab);
        Label nicknameLabel = createNicknameLabel(q, nn);
        Label questionLabel = createQuestionLabel(q.getQuestion(), tab);

        ImageView upvoteButton = createUpvoteButton(q.isUpvoted());
        Label upvotes = createUpvoteCount(q);

        MenuButton moreOptions = createMoreOptionsButton();

        HBox answerGui = createAnswerGui(questionPane);
        TextArea answer = createAnswerBox();
        ImageView submitAnswer = createSubmitAnswerButton(answer);

        HBox editGui = createEditGui(questionPane);
        TextArea edit = createEditBox(q.getQuestion());
        ImageView submitEdit = createSubmitEditButton(edit);

        answerGui.getChildren().addAll(answer, submitAnswer);
        editGui.getChildren().addAll(edit,submitEdit);

        // Edit and delete menu items
        MenuItem deleteOption = new MenuItem("Delete");

        EventHandler<MouseEvent> markAsAnswered = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                ServerCommunication.markQuestionAsAnswered(q.getId(), nickname.getId());
                getAllQuestions();
            }
        };
        EventHandler<ActionEvent> deleteQuestion = new EventHandler<>() {
            public void handle(ActionEvent event) {
                ServerCommunication.deleteQuestion(q.getId(), nickname.getId());
                getAllQuestions();
            }
        };
        deleteOption.setOnAction(deleteQuestion);

        ImageView markAsAnsweredButton = createMarkAsAnsweredButton();
        markAsAnsweredButton.addEventHandler(MouseEvent.MOUSE_CLICKED, markAsAnswered);

        if (nickname.isModerator() || nickname.getId() == q.getNicknameId()) {
            questionPane.getChildren().addAll(nicknameLabel,
                    questionLabel,
                    upvoteButton,
                    upvotes,
                    moreOptions,
                    markAsAnsweredButton);
        } else {
            questionPane.getChildren().addAll(nicknameLabel,
                    questionLabel,
                    upvoteButton,
                    upvotes);
        }

        MenuItem answerOption = new MenuItem("Answer");
        MenuItem editOption = new MenuItem("Edit");

        if (nickname.isModerator() && q.getNicknameId() == nickname.getId()) {
            moreOptions.getItems().addAll(createMuteButton(nn), editOption);
        }
        moreOptions.getItems().addAll(answerOption, deleteOption);

        EventHandler<MouseEvent> upvoteQuestion = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                ServerCommunication.upvoteQuestion(q.getId(), nickname.getId());
                getAllQuestions();
            }
        };

        upvoteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, upvoteQuestion);
        upvotes.addEventHandler(MouseEvent.MOUSE_CLICKED, upvoteQuestion);
   

        // Open an answer to a question event
        EventHandler<ActionEvent> answerToQuestion = event -> {
            //tab.getChildren().add(answerGui);
            addAnswerGui(answerGui, tab, q.getId());
        };
        answerOption.setOnAction(answerToQuestion);

        EventHandler<ActionEvent> editQuestion = event ->  {
            tab.getChildren().add(editGui);
        };
        editOption.setOnAction(editQuestion);

        EventHandler<MouseEvent> editAnQuestion = event -> {
            tab.getChildren().removeAll(editGui);
            if (edit.getText().isBlank() || edit.getText() == edit.getText()) {
                edit.clear();
                edit.setStyle("-fx-prompt-text-fill: red;");
                edit.setPromptText("You have to edit the question!");
                return;
            }
            ServerCommunication.editQuestion(q.getId(), nickname.getId(), edit.getText());
            getAllQuestions();
        };
        submitEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, editAnQuestion);

        // Submit an answer to a question event
        EventHandler<MouseEvent> submitAnAnswer = event -> {
            tab.getChildren().removeAll(answerGui);
            if (answer.getText().isBlank()) {
                answer.setStyle("-fx-prompt-text-fill: red;");
                answer.setPromptText("You have to fill in a answer!");
                return;
            }
            ServerCommunication.answerQuestion(q.getId(), nickname.getId(), answer.getText());
            getAllQuestions();
        };
        submitAnswer.addEventHandler(MouseEvent.MOUSE_CLICKED, submitAnAnswer);

        EventHandler<KeyEvent> enter = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    tab.getChildren().removeAll(answerGui);
                    ServerCommunication.answerQuestion(q.getId(),
                            nickname.getId(), answer.getText());
                    getAllQuestions();
                }
            }
        };
        answer.addEventHandler(KeyEvent.KEY_PRESSED, enter);

        tab.getChildren().add(questionPane);
        if (q.getAnswer() != null) {
            tab.getChildren().add(getAnswerHBox(q.getQuestion(),
                    q.getAnswer(), nicknameLabel.getText()));
        }
    }


    private void addAnswerGui(HBox answerGui, VBox tab, long qid) {
        tab.getChildren().clear();
        List<Question> questionList = ServerCommunication.getQuestions(this.room.getUrl(),
                false,false, nickname.getId());

        for (Question q : questionList) {
            createLayout(q, tab);
            if (q.getId() == qid) {
                tab.getChildren().add(answerGui);
            }
        }
    }


    private HBox getAnswerHBox(String question, String answer, String answeredBy) {
        int answerBoxPaddingLeft = 20;
        Pane answerPane = createPane();
        answerPane.prefWidthProperty().bind(
                questionBox.widthProperty().subtract(answerBoxPaddingLeft));
        Label nicknameAnswerLabel = createAnsweringNicknameLabel(question, answeredBy);

        HBox answerContainer = new HBox();
        answerContainer.setPadding(new Insets(0, 0, 0, answerBoxPaddingLeft));
        answerContainer.getChildren().add(answerPane);

        Label answerLabel = createAnswerLabel(answer);
        answerPane.getChildren().addAll(nicknameAnswerLabel, answerLabel);
        return answerContainer;

    }


    /**
     * A specialised layout for the answered questions tab.
     * @param q the question that is answered
     * @param tab the main container for the panes
     */
    private void createAnsweredLayout(Question q, VBox tab) {
        Pane questionPane = createPane(tab);
        Label nicknameLabel = createNicknameLabel(q, nickname);
        Label questionLabel = createQuestionLabel(q.getQuestion(), tab);
        questionPane.getChildren().addAll(nicknameLabel,
                questionLabel);
        tab.getChildren().add(questionPane);
        if (q.getAnswer() != null) {
            tab.getChildren().add(getAnswerHBox(q.getQuestion(),
                    q.getAnswer(), nicknameLabel.getText()));
        }
    }

    private MenuItem createMuteButton(Nickname nn) {
        MenuItem muteOption = new MenuItem("Mute");

        EventHandler<ActionEvent> mute = new EventHandler<>() {
            public void handle(ActionEvent event) {
                ServerCommunication.muteNickname(nn.getId(), nickname.getId());
            }
        };

        muteOption.setOnAction(mute);
        return muteOption;
    }

    /**
     * Creates a Pane onto which all the elements will be added.
     * @param tab - the Vbox into which the Pane is created.
     * @return the Pane
     */
    private Pane createPane(VBox tab) {
        // Create Pane
        Pane questionPane = new Pane();
        questionPane.setStyle("-fx-background-color: white");

        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        questionPane.setEffect(shadow);

        questionPane.heightProperty().addListener((ObservableValue<? extends Number> observable,
                                                   Number oldValue, Number newValue) -> {
            questionBox.getHeight(); });
        questionPane.translateYProperty().addListener((ObservableValue<? extends Number> observable,
                                                       Number oldValue, Number newValue) -> {
            questionBox.getHeight(); });

        return questionPane;
    }

    private Pane createPane() {
        // Create Pane
        Pane questionPane = new Pane();
        questionPane.setStyle("-fx-background-color: white");

        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        questionPane.setEffect(shadow);

        questionPane.heightProperty().addListener((ObservableValue<? extends Number> observable,
                                                   Number oldValue, Number newValue) -> {
            questionBox.getHeight(); });
        questionPane.translateYProperty().addListener((ObservableValue<? extends Number> observable,
                                                       Number oldValue, Number newValue) -> {
            questionBox.getHeight(); });

        return questionPane;
    }

    /**
     * Creates the nickname Label.
     * @param q - the Question from which the Nickname is fetched.
     * @return the nickname Label
     */
    private Label createNicknameLabel(Question q, Nickname nn) {
        Label nicknameLabel = new Label(nn.getName());
        nicknameLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        nicknameLabel.setTextFill(Color.valueOf("#00a6d6"));
        nicknameLabel.setStyle("-fx-padding: 3px, 3px, 3px, 3px");
        return nicknameLabel;
    }

    /**
     * Creates the nickname Label to a answer.
     * @param question - the Question from which the Nickname is fetched.
     * @param answered - the actual question that is answered.
     * @return the nickname Label
     */
    private Label createAnsweringNicknameLabel(String question, String answered) {
        //String answeredName = nicknameLabel.getText();
        //String answeredLabel = questionLabel.getText();
        Label answeringLabel = new Label(nickname.getName());
        answeringLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 12));
        answeringLabel.setTextFill(Color.valueOf("#00a6d6"));
        answeringLabel.setStyle("-fx-padding: 3px, 3px, 3px, 3px");
        return answeringLabel;
    }

    /**
     * Creates the question Label.
     * @param q - the Question of which the Label will be created.
     * @param tab - the Vbox into which the layout is created.
     * @return the question Label
     */
    private Label createQuestionLabel(String q, VBox tab) {
        Label question = new Label(q);
        question.setLayoutY(20);
        question.setMaxWidth(730);
        question.setStyle("-fx-padding: 3px, 3px, 3px, 3px");
        question.setWrapText(true);
        if (tab.equals(questions)) {
            question.setFont(Font.font("Trebuchet MS",12));
        } else if (tab.equals(presenterMode)) {
            question.setFont(Font.font("Trebuchet MS", 20));
        }
        return question;
    }

    /**
     * Creates the upvote button.
     * @return the upvote button (ImageView).
     */
    private ImageView createUpvoteButton(boolean upvoted) {
        String url;
        if (upvoted) {
            url = "icons/upvote.jpg";
        } else {
            url = "icons/upvotegrey.png";
        }
        ImageView upvoteButton = new ImageView(url);
        upvoteButton.setFitWidth(16);
        upvoteButton.setFitHeight(21);
        upvoteButton.setLayoutX(764);
        upvoteButton.setLayoutY(4);
        upvoteButton.setCursor(Cursor.HAND);
        return upvoteButton;
    }

    /**
     * Creates a Label for the upvote count of a particular Question.
     * @param q - the Question which's upvotes will be considered.
     * @return the upvote count Label
     */
    private Label createUpvoteCount(Question q) {
        Label upvotes = new Label(String.valueOf(q.getUpvotes()));
        upvotes.setFont(Font.font("Trebuchet MS", 12));
        upvotes.setLayoutX(769);
        upvotes.setLayoutY(24);
        upvotes.setCursor(Cursor.HAND);
        return upvotes;
    }

    /**
     * Creates the more options MenuButton.
     * @return the more options MenuButton
     */
    private MenuButton createMoreOptionsButton() {
        MenuButton moreOptions = new MenuButton();
        moreOptions.setStyle("-fx-background-color: white");
        moreOptions.setLayoutX(732);
        moreOptions.setLayoutY(8);
        moreOptions.setCursor(Cursor.HAND);

        ImageView img = new ImageView(new Image("icons/moreOptions.png"));
        img.setFitWidth(16);
        img.setFitHeight(16);
        moreOptions.setGraphic(img);

        return moreOptions;
    }

    /**
     * Creates the mark as answered button.
     * @return the mark as answered button (ImageView)
     */
    private ImageView createMarkAsAnsweredButton() {
        ImageView markAsAnsweredButton = new ImageView("icons/answered.png");
        markAsAnsweredButton.setFitWidth(16);
        markAsAnsweredButton.setFitHeight(16);
        markAsAnsweredButton.setLayoutX(718);
        markAsAnsweredButton.setLayoutY(11);
        markAsAnsweredButton.setCursor(Cursor.HAND);
        return markAsAnsweredButton;
    }

    /**
     * Creates the textarea for a answer.
     * @return the textarea
     */
    private TextArea createAnswerBox() {
        TextArea answer = new TextArea();
        answer.toFront();
        answer.setStyle("-fx-background-color: BLACK");

        answer.setPrefRowCount(1);
        answer.setPrefColumnCount(60);
        answer.setWrapText(true);
        answer.setStyle("-fx-vbar-policy: never");

        SimpleIntegerProperty count = new SimpleIntegerProperty(30);
        int rowHeight = 10;

        // Auto resizing of the textarea
        answer.prefHeightProperty().bindBidirectional(count);
        answer.minHeightProperty().bindBidirectional(count);
        answer.scrollTopProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal.intValue() > rowHeight) {
                count.setValue(count.get() + newVal.intValue());
            }
        });

        // Set prompt text
        answer.setPromptText("ANSWER THEIR QUESTION");

        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        answer.setEffect(shadow);

        return answer;
    }

    /**
     * Creates the textarea for a answer.
     * @return the textarea
     */
    private TextArea createEditBox(String question) {
        TextArea edit = new TextArea(question);

        edit.setPrefRowCount(1);
        edit.setPrefColumnCount(60);
        edit.setWrapText(true);
        edit.setFont(Font.font("Trebuchet MS",17));
        edit.setStyle("-fx-vbar-policy: never");

        SimpleIntegerProperty count = new SimpleIntegerProperty(30);
        int rowHeight = 10;

        // Auto resizing of the textarea
        edit.prefHeightProperty().bindBidirectional(count);
        edit.minHeightProperty().bindBidirectional(count);
        edit.scrollTopProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal.intValue() > rowHeight) {
                count.setValue(count.get() + newVal.intValue());
            }
        });
        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        edit.setEffect(shadow);

        return edit;
    }

    /**
     * Creates the main container for the all the answering nodes,
     * which the Y property is calculated.
     * @param questionPane The corresponding pane to this answer.
     * @return the HBox node container
     */
    private HBox createAnswerGui(Pane questionPane) {
        HBox answerGui = new HBox();
        answerGui.setLayoutY(questionPane.getHeight() + 10);
        answerGui.setPadding(new Insets(0, 0, 0, 20));
        return answerGui;
    }

    private HBox createEditGui(Pane questionPane) {
        HBox answerGui = new HBox();
        answerGui.setPadding(new Insets(0, 0, 0, 20));
        return answerGui;
    }

    private ImageView createSubmitAnswerButton(TextArea answer) {
        ImageView submitButton = new ImageView("icons/send.png");
        submitButton.setFitWidth(40);
        submitButton.setFitHeight(40);
        submitButton.setLayoutX(answer.getLayoutX());
        submitButton.setLayoutY(answer.getLayoutY());
        submitButton.setCursor(Cursor.HAND);
        submitButton.setRotate(5);
        submitButton.setPickOnBounds(true);
        return submitButton;
    }

    private ImageView createSubmitEditButton(TextArea edit) {
        ImageView submitButton = new ImageView("icons/edit.png");
        submitButton.setFitWidth(40);
        submitButton.setFitHeight(40);
        submitButton.setLayoutX(edit.getLayoutX());
        submitButton.setLayoutY(edit.getLayoutY());
        submitButton.setCursor(Cursor.HAND);
        submitButton.setRotate(5);
        submitButton.setPickOnBounds(true);
        return submitButton;
    }

    private Label createAnswerLabel(String anAnswer) {
        Label answer = new Label(anAnswer);
        answer.setLayoutY(20);
        answer.setMaxWidth(730);
        answer.setStyle("-fx-padding: 3px, 3px, 3px, 3px");
        answer.setWrapText(true);
        answer.setFont(Font.font("Trebuchet MS",12));
        return answer;
    }

    /**
     * Prints all the questions from the database (for this room) onto the screen.
     */
    public void getAllQuestions() {
        questions.getChildren().clear();

        List<Question> questionList = ServerCommunication.getQuestions(this.room.getUrl(),
                false,false, nickname.getId());

        questions.setPrefHeight(
                Math.max(513, Math.min(questionList.size(), questionList.size()) * 100)
        );

        for (Question q : questionList) {
            createLayout(q, questions);
        }
        getTopQuestions();
        getAnsweredQuestions();
        setParticipants();
        refreshRoom();
        refreshNickname();
        getPolls();
    }

    /**
     * Downloads the answered questions into a Csv file.
     */
    public void downloadCsv() {
        ServerCommunication.downloadAnsweredQuestionsAsCsv(this.room.getUrl(),this.stage);
    }

    /**
     * Prints the top five (most upvotes) questions into the presenter mode tab.
     */
    private void getTopQuestions() {
        presenterMode.getChildren().clear();
        List<Question> questionList = ServerCommunication.getQuestions(this.room.getUrl(),
                true,false, nickname.getId());

        presenterMode.setPrefHeight(
                Math.max(513, Math.min(topQuestionsAmount, questionList.size()) * 100)
        );
        int i = 0;
        while (i < topQuestionsAmount && i < questionList.size()) {
            createLayout(questionList.get(i), presenterMode);
            i++;
        }
        //getPolls();
    }

    private void getPolls() {
        if (!nickname.isModerator()) {
            createPollButton.setDisable(true);
            createPollButton.setVisible(false);
        }
        Node labelP = polls.getChildren().get(0);
        polls.getChildren().clear();
        polls.getChildren().add(labelP);

        Node labelPR = pollResults.getChildren().get(0);
        pollResults.getChildren().clear();
        pollResults.getChildren().add(labelPR);
        List<Poll> polls = ServerCommunication.getPolls(this.room.getUrl(), this.nickname.getId());

        for (Poll p : polls) {
            createPollLayout(p);
            createPollResultsLayout(p);
        }
    }

    private void createPollLayout(Poll poll) {
        Pane pollPane = createPollPane(poll.amountOfOptions());
        Pane titleLabelAndButtons = createPollLabelAndButtons(poll);
        List<Button> buttons = createOptionButtons(poll);

        pollPane.getChildren().add(titleLabelAndButtons);
        pollPane.getChildren().addAll(buttons);

        polls.getChildren().add(pollPane);
    }

    private Pane createPollPane(int amountOfOptions) {
        //Create pane
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: white");

        //Shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        pane.setEffect(shadow);

        double height = 29 + ((int) ((amountOfOptions + 1) / 2)) * 39;
        pane.setPrefHeight(height);

        return pane;
    }

    private Label createPollLabel(Poll p) {
        String title = p.getTitle();
        Label pollLabel = new Label(title);

        pollLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        pollLabel.setTextFill(Color.valueOf("#00a6d6"));
        pollLabel.setStyle("-fx-padding: 3px, 3px, 3px, 3px");

        return pollLabel;
    }

    private Pane createPollLabelAndButtons(Poll p) {
        Pane pane = new Pane();
        pane.setPrefSize(480, 25);

        String title = p.getTitle();
        Label pollLabel = new Label(title);

        pollLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        pollLabel.setTextFill(Color.valueOf("#00a6d6"));
        pollLabel.setStyle("-fx-padding: 3px, 3px, 3px, 3px");

        pane.getChildren().add(pollLabel);

        if (nickname.isModerator()) {
            ImageView deleteButton = new ImageView("icons/delete.png");

            EventHandler<MouseEvent> deletePoll = new EventHandler<>() {
                @Override
                public void handle(MouseEvent event) {
                    ServerCommunication.deletePoll(p.getId(), nickname.getId());
                    getPolls();
                }
            };

            deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, deletePoll);

            deleteButton.setFitHeight(20);
            deleteButton.setFitWidth(20);
            deleteButton.setLayoutX(420);
            deleteButton.setLayoutY(2);
            deleteButton.setCursor(Cursor.HAND);
            deleteButton.setPickOnBounds(true);

            ImageView opencloseButton;
            if (p.isActive()) {
                opencloseButton = new ImageView("icons/close.jpg");
            } else {
                opencloseButton = new ImageView("icons/answered.png");
            }

            EventHandler<MouseEvent> openClosePoll = new EventHandler<>() {
                @Override
                public void handle(MouseEvent event) {
                    ServerCommunication.openClosePoll(p.getId(), !p.isActive(), nickname.getId());
                    getPolls();
                }
            };

            opencloseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, openClosePoll);

            opencloseButton.setFitHeight(20);
            opencloseButton.setFitWidth(20);
            opencloseButton.setLayoutX(450);
            opencloseButton.setLayoutY(2);
            opencloseButton.setCursor(Cursor.HAND);
            opencloseButton.setPickOnBounds(true);

            pane.getChildren().addAll(deleteButton, opencloseButton);
        }


        return pane;
    }

    private List<Button> createOptionButtons(Poll p) {
        List<String> options = Arrays.asList(
                p.getOption1(),
                p.getOption2(),
                p.getOption3(),
                p.getOption4(),
                p.getOption5(),
                p.getOption6()
        );

        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < p.amountOfOptions(); i++) {
            Button b = createOptionButton(options.get(i), i + 1, p.getId());
            b.setLayoutX(((i) % 2) * 235 + 10);
            b.setLayoutY((((int) i / 2)) * 40 + 25);

            if (p.isAnswered() || !p.isActive()) {
                b.setDisable(true);
            }

            buttons.add(b);
        }
        return buttons;
    }

    private Button createOptionButton(String option, int optionNumber, long pollId) {
        Button b = new Button(option);
        b.setPrefSize(225, 33);
        b.setStyle("-fx-background-color: #0087e0; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-font-family: 'Trebuchet MS';");
        b.setCursor(Cursor.HAND);

        EventHandler<MouseEvent> clickOption = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                ServerCommunication.answerPoll(pollId, optionNumber, nickname.getId());
                getAllQuestions();
            }
        };

        b.addEventHandler(MouseEvent.MOUSE_CLICKED, clickOption);

        return b;
    }

    private void createPollResultsLayout(Poll poll) {
        Pane pollResultPane = createPollPane(poll.amountOfOptions());
        Label pollResultTitleLabel = createPollLabel(poll);
        List<Label> labels = createResultLabels(poll);

        pollResultPane.getChildren().add(pollResultTitleLabel);
        pollResultPane.getChildren().addAll(labels);

        pollResults.getChildren().add(pollResultPane);
    }

    private List<Label> createResultLabels(Poll p) {
        List<Integer> results = ServerCommunication.getResults(p.getId());

        List<Label> labels = new ArrayList<>();

        for (int i = 0; i < p.amountOfOptions(); i++) {
            Label l = createResultLabel(results.get(i), i + 1);
            l.setLayoutX(((i) % 2) * 135 + 10);
            l.setLayoutY((((int) i / 2)) * 40 + 25);


            labels.add(l);
        }
        return labels;
    }

    private Label createResultLabel(int result, int optionNumber) {
        Label l = new Label(Integer.toString(result) + '%');
        l.setPrefSize(125, 33);
        l.setStyle("-fx-text-fill: #0087e0; -fx-border-color: #0087e0; "
                + "-fx-border-width: 3; -fx-alignment: center;");
        l.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        return l;
    }

    /**
     * Create layout for creating a new poll.
     */
    public void createNewPollLayout() {
        Label newPollLabel = new Label("New poll:");

        newPollLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        newPollLabel.setTextFill(Color.valueOf("#00a6d6"));
        newPollLabel.setStyle("-fx-padding: 3px, 3px, 3px, 3px");

        Button submitButton = createNewPollSubmitButton();
        List<TextField> textFields = createNewPollTextFields();

        EventHandler<MouseEvent> createPoll = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                createPoll(textFields);
            }
        };
        EventHandler<KeyEvent> createPollEnter = new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    createPoll(textFields);
                }
            }
        };

        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, createPoll);
        for (TextField tf : textFields) {
            tf.addEventHandler(KeyEvent.KEY_PRESSED, createPollEnter);
        }

        Pane newPollPane = createPollPane(10);
        newPollPane.getChildren().add(newPollLabel);
        newPollPane.getChildren().addAll(textFields);
        newPollPane.getChildren().add(submitButton);

        List<Node> nodes = new ArrayList<>();
        ObservableList<Node> children = polls.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            nodes.add(children.remove(0));
            if (i == 0) {
                nodes.add(newPollPane);
            }
        }
        polls.getChildren().setAll(nodes);
    }

    private List<TextField> createNewPollTextFields() {
        TextField titleTextField = new TextField();
        titleTextField.setPromptText("Title");
        titleTextField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);");
        titleTextField.setPrefSize(225,  33);
        titleTextField.setLayoutX(10);
        titleTextField.setLayoutY(25);

        List<TextField> textFields = new ArrayList<>();
        textFields.add(titleTextField);

        for (int i = 0; i < 6; i++) {
            TextField tf = new TextField();

            String prompText = "Option " + (i + 1);
            if (i >= 2) {
                prompText += " (optional)";
            }
            tf.setPromptText(prompText);

            tf.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);");

            tf.setPrefSize(225,  33);
            tf.setLayoutX(((i) % 2) * 235 + 10);
            tf.setLayoutY((((int) i / 2)) * 40 + 65);

            textFields.add(tf);
        }
        return textFields;
    }

    private Button createNewPollSubmitButton() {
        Button b = new Button("CREATE");

        b.setPrefSize(109, 25);
        b.setLayoutX(360);
        b.setLayoutY(183);
        b.setStyle("-fx-background-color: #00a6d6; -fx-border-color: #0087e0; -fx-border-width: 2; "
                + "-fx-border-radius: 20; -fx-background-radius: 20; -fx-text-fill: white");
        b.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 16));
        b.setCursor(Cursor.HAND);

        return b;
    }

    private void createPoll(List<TextField> textFields) {
        String title = textFields.get(0).getText();

        List<Integer> errors = new ArrayList<>();

        if (title.equals("")) {
            errors.add(0);
        }

        String option1 = textFields.get(1).getText();
        if (option1.equals("")) {
            errors.add(1);
        }

        String option2 = textFields.get(2).getText();
        if (option2.equals("")) {
            errors.add(2);
        }

        if (errors.size() > 0) {
            for (int i : errors) {
                textFields.get(i).setStyle("-fx-prompt-text-fill: red;");
            }
            return;
        }

        String option3 = textFields.get(3).getText();
        if (option3.equals("")) {
            option3 = null;
        }

        String option4 = textFields.get(4).getText();
        if (option4.equals("")) {
            option4 = null;
        }

        String option5 = textFields.get(5).getText();
        if (option5.equals("")) {
            option5 = null;
        }

        String option6 = textFields.get(6).getText();
        if (option6.equals("")) {
            option6 = null;
        }

        Poll poll = new Poll(0, this.room.getUrl(), title,
                option1, option2, option3, option4, option5, option6,
                true, false);

        ServerCommunication.createPoll(poll);
        getPolls();
    }

    private void getAnsweredQuestions() {
        answeredQuestions.getChildren().clear();
        List<Question> questionList = ServerCommunication.getQuestions(this.room.getUrl(),
                false,true, nickname.getId());

        answeredQuestions.setPrefHeight(
                Math.max(513, Math.min(questionList.size(), questionList.size()) * 100)
        );

        for (Question q : questionList) {
            createAnsweredLayout(q, answeredQuestions);
        }
    }

    /**
     * Get the feedback from backend and generates the graph view of feedbacks.
     */
    private void initializeFeedBackScreen() {
        if (commonCooldownPeriod <= 0) {
            giveFeedbackTab.getChildren().clear();
            VBox overAllContainer = new VBox();
            overAllContainer.setAlignment(Pos.CENTER);
            overAllContainer.setPrefWidth(giveFeedbackTab.getPrefWidth());
            HBox chartContainer = new HBox();
            chartContainer.setAlignment(Pos.CENTER);
            HBox labelContainer = new HBox();
            labelContainer.setPadding(new Insets(0, 0, 0, 50));
            renderPieChart(chartContainer);
            Label coolDownLabel = new Label();
            labelContainer.getChildren().add(coolDownLabel);
            labelContainer.setAlignment(Pos.CENTER);
            overAllContainer.getChildren().add(chartContainer);
            overAllContainer.getChildren().add(renderFeedBackButtons(chartContainer,
                    coolDownLabel));
            overAllContainer.getChildren().add(labelContainer);
            giveFeedbackTab.getChildren().add(overAllContainer);
        } else {
            refreshPieChart();
        }
    }

    /**
     * Creates buttons for voting.
     * @return buttons for voting
     */
    private HBox renderFeedBackButtons(HBox chartContainer, Label coolDownLabel) {
        Button btnSlower = new Button("Slow down",
                new ImageView("/buttons/slow-feedback-btn.png"));
        Button btnJustRight = new Button("Just Right",
                new ImageView("/buttons/just-right-feedback-btn.png"));
        Button btnFast = new Button("Faster",
                new ImageView("/buttons/fast-feedback-btn.png"));
        btnSlower.setCursor(Cursor.HAND);
        btnJustRight.setCursor(Cursor.HAND);
        btnFast.setCursor(Cursor.HAND);
        List<Node> buttonArray = Arrays.asList(btnSlower, btnJustRight, btnFast);
        btnSlower.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
                ServerCommunication.createFeedback(room.getUrl(),
                        FeedbackType.SLOWER.toString(), FeedbackType.SLOWER, nickname.getId());
                renderPieChart(chartContainer);
                setCooldown(buttonArray, coolDownLabel, GIVE_FEEDBACK_COOL_DOWN_PERIOD);
            }
        });

        btnJustRight.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
                ServerCommunication.createFeedback(room.getUrl(),
                        FeedbackType.JUST_RIGHT.toString(),
                        FeedbackType.JUST_RIGHT, nickname.getId());
                renderPieChart(chartContainer);
                setCooldown(buttonArray, coolDownLabel, GIVE_FEEDBACK_COOL_DOWN_PERIOD);
            }
        });

        btnFast.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
                ServerCommunication.createFeedback(room.getUrl(),
                        FeedbackType.FASTER.toString(), FeedbackType.FASTER, nickname.getId());
                renderPieChart(chartContainer);
                setCooldown(buttonArray, coolDownLabel, GIVE_FEEDBACK_COOL_DOWN_PERIOD);
            }
        });

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(btnSlower);
        buttonContainer.getChildren().add(btnJustRight);
        buttonContainer.getChildren().add(btnFast);

        if (commonCooldownPeriod > 0) {
            setCooldown(buttonArray, coolDownLabel, commonCooldownPeriod);
        }
        return buttonContainer;
    }

    /**
     *  Renders Pie chart.
     * @param piechartContainer pie chart client side
     */
    public void renderPieChart(HBox piechartContainer) {
        if (storedPieChart != null) {
            System.out.println(commonCooldownPeriod);
            if (piechartContainer.getChildren().size() == 0) {
                piechartContainer.getChildren().add(storedPieChart);
            }
            refreshPieChart();
            return;
        }
        piechartContainer.getChildren().clear();
        List<Feedback> feedBacks = ServerCommunication.getFeedbackByUrl(room.getUrl());
        Integer slowerFeedbackCount = 0;
        Integer justRightFeedbackCount = 0;
        Integer fasterFeedbackCount = 0;
        for (Feedback feedback : feedBacks) {
            switch (feedback.getFeedbackType()) {
                case SLOWER:
                    slowerFeedbackCount++;
                    break;
                case JUST_RIGHT:
                    justRightFeedbackCount++;
                    break;
                case FASTER:
                    fasterFeedbackCount++;
                    break;
                default:
                    break;
            }
        }

        if (slowerFeedbackCount == 0 && justRightFeedbackCount == 0 && fasterFeedbackCount == 0) {
            HBox labelContainer = new HBox();
            labelContainer.setPadding(new Insets(100, 0, 100, 0));
            labelContainer.getChildren().add(new Label("No data available"));
            piechartContainer.getChildren().add(labelContainer);
            return;
        }

        PieChart pieChart = new PieChart();

        PieChart.Data slice1 = null;
        PieChart.Data slice2 = null;
        PieChart.Data slice3 = null;
        if (slowerFeedbackCount > 0) {
            slice1 = new PieChart.Data("SLOWER (" + slowerFeedbackCount + ")", slowerFeedbackCount);
            pieChart.getData().add(slice1);
        }
        if (justRightFeedbackCount > 0) {
            slice2 = new PieChart.Data("JUST RIGHT (" + justRightFeedbackCount + ")",
                    justRightFeedbackCount);
            pieChart.getData().add(slice2);
        }
        if (fasterFeedbackCount > 0) {
            slice3 = new PieChart.Data("FASTER (" + fasterFeedbackCount + ")", fasterFeedbackCount);
            pieChart.getData().add(slice3);
        }
        if (slice1 != null) {
            slice1.getNode().setStyle("-fx-pie-color: red");
        }
        if (slice2 != null) {
            slice2.getNode().setStyle("-fx-pie-color: green");
        }
        if (slice3 != null) {
            slice3.getNode().setStyle("-fx-pie-color: blue");
        }
        pieChart.setLegendVisible(false);

        this.storedPieChart = pieChart;

        piechartContainer.getChildren().add(pieChart);
    }

    /**
     * Refreshes the pie chart.
     */
    public void refreshPieChart() {
        if (storedPieChart == null) {
            System.out.println("n");
            return;
        }

        List<Feedback> feedBacks = ServerCommunication.getFeedbackByUrl(room.getUrl());
        Integer slowerFeedbackCount = 0;
        Integer justRightFeedbackCount = 0;
        Integer fasterFeedbackCount = 0;
        for (Feedback feedback : feedBacks) {
            switch (feedback.getFeedbackType()) {
                case SLOWER:
                    slowerFeedbackCount++;
                    break;
                case JUST_RIGHT:
                    justRightFeedbackCount++;
                    break;
                case FASTER:
                    fasterFeedbackCount++;
                    break;
                default:
                    break;
            }
        }

        storedPieChart.getData().clear();

        PieChart.Data slice1 = null;
        PieChart.Data slice2 = null;
        PieChart.Data slice3 = null;
        if (slowerFeedbackCount > 0) {
            slice1 = new PieChart.Data("SLOWER (" + slowerFeedbackCount + ")", slowerFeedbackCount);
            storedPieChart.getData().add(slice1);
        }
        if (justRightFeedbackCount > 0) {
            slice2 = new PieChart.Data("JUST RIGHT (" + justRightFeedbackCount + ")",
                    justRightFeedbackCount);
            storedPieChart.getData().add(slice2);
        }
        if (fasterFeedbackCount > 0) {
            slice3 = new PieChart.Data("FASTER (" + fasterFeedbackCount + ")", fasterFeedbackCount);
            storedPieChart.getData().add(slice3);
        }
        if (slice1 != null) {
            slice1.getNode().setStyle("-fx-pie-color: red");
        }
        if (slice2 != null) {
            slice2.getNode().setStyle("-fx-pie-color: green");
        }
        if (slice3 != null) {
            slice3.getNode().setStyle("-fx-pie-color: blue");
        }
    }

    public void handleRefreshButtonClick() {
        getAllQuestions();
        refreshPieChart();
    }

    public void openHelp() {
        helpTab.toFront();
    }

    public void closeHelp() {
        helpTab.toBack();
    }

    /**
     * Opens the settings tab.
     */
    public void openSettings() {
        settingsTab.toFront();
    }

    /**
     * Closes the settings tab.
     */
    public void closeSettings() {
        settingsTab.toBack();
        errorLabel.setVisible(false);
        initData(this.room, this.nickname);
    }

    /**
     * Applies changes made in the settings tab.
     */
    public void applyChanges() {
        // Nickname change
        if (changeNickname.getText().isBlank()) {
            String newNickname = "Empty";
        }
        String newNickname = changeNickname.getText();

        if (!this.nickname.getName().equals(newNickname)) {
            if (ServerCommunication.getNickname(this.room.getUrl(), newNickname) != null) {
                errorLabel.setVisible(true);
                return;
            }

            this.nickname.setName(newNickname);
            ServerCommunication.changeNickname(this.nickname);
        }

        // Top questions amount
        if (topQuestionsAmountTF.getText().isBlank()) {
            topQuestionsAmount = 5;
        }
        topQuestionsAmount = Integer.parseInt(topQuestionsAmountTF.getText());

        // Slow mode
        if (slowModeOn.isSelected()) {
            if (questionCooldownTF.getText().isBlank()) {
                int cd = 30;
            }
            int cd = Integer.parseInt(questionCooldownTF.getText());
            ServerCommunication.setCooldown(this.room, cd, this.nickname.getId());
        } else {
            ServerCommunication.setCooldown(this.room, 0, this.nickname.getId());
        }

        // Feedback reset
        // CAN'T IMPLEMENT YET, FEEDBACK MR NEEDS TO BE MERGED FIRST.

        closeSettings();
    }

    /**
     * Hides question cooldown Label and TextField.
     */
    public void hideCooldown() {
        if (!slowModeOff.isSelected()) {
            slowModeOff.setSelected(true);
        }
        questionCooldownLabel.setVisible(false);
        questionCooldownTF.setVisible(false);

        switchColours(slowModeOff,slowModeOn);
    }

    /**
     * Makes question cooldown Label and TextField visible.
     */
    public void showCooldown() {
        if (!slowModeOn.isSelected()) {
            slowModeOn.setSelected(true);
        }
        questionCooldownLabel.setVisible(true);
        questionCooldownTF.setVisible(true);

        switchColours(slowModeOn,slowModeOff);
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
}
