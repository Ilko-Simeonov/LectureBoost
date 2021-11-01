package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import org.w3c.dom.Text;

public class TestSceneController {
    @FXML
    private ListView questionList;


    //Text Fields.
    @FXML
    private TextField createTextFieldUrl;

    @FXML
    private TextField createTextFieldQuestion;

    @FXML
    private TextField nidTextField;

    @FXML
    private TextField getTextFieldUrl;

    @FXML
    private TextField getOrderedTextFieldUrl;

    @FXML
    private TextField deleteTextFieldId;

    @FXML
    private TextField nidDelTextFieldId;

    @FXML
    private TextField upvoteTextFieldId;

    @FXML
    private TextField nidAnswerTextField;

    @FXML
    private TextField createRoomName;

    @FXML
    private TextField modPasswordTextField;

    @FXML
    private TextField deleteRoomId;

    @FXML
    private TextField openRoomId;


    //Checkboxes.
    @FXML
    private CheckBox createRoomOpen;

    /**
     * Create new Question from question-textField, with url from url-textField.
     */
    public void createQuestion() {
        String url = createTextFieldUrl.getText();
        String question = createTextFieldQuestion.getText();
        long nid = Long.parseLong(nidTextField.getText());
        ServerCommunication.createQuestion(url, question, nid);
    }

    /**
     * Adds all questions for a certain url (from text field) to the listview.
     * If the url is empty, gets all the questions from every url.
     */
    public void getQuestions() {
        String url = getTextFieldUrl.getText();
        List<Question> questions = ServerCommunication.getQuestions(url, false, false, 0);
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }


    /**
     * Adds all questions for a certain url (from text field) to the listview ordered by upvotes.
     */
    public void getQuestionsOrdered() {
        String url = getOrderedTextFieldUrl.getText();
        List<Question> questions = ServerCommunication.getQuestions(url, true, false, 0);
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Adds all answered questions for a certain url (from text field) to the listview.
     * If the url is empty, gets all the questions from every url.
     */
    public void getQuestionsAnswered() {
        String url = getOrderedTextFieldUrl.getText();
        List<Question> questions = ServerCommunication.getQuestions(url, false, true, 0);
        questionList.getItems().clear();
        questionList.getItems().addAll(questions);
    }

    /**
     * Delete question with a certain id (from text field).
     */
    public void deleteQuestion() {
        long qid = Long.parseLong(deleteTextFieldId.getText());
        long nid = Long.parseLong(nidDelTextFieldId.getText());
        ServerCommunication.deleteQuestion(qid, nid);
    }

    /**
     * Upvote a question with a certain id (from text field).
     */
    public void upvote() {
        long id = Long.parseLong(upvoteTextFieldId.getText());
        ServerCommunication.upvoteQuestion(id, 0);
    }

    /**
     * Mark a question with a certain id (from text field) as answered.
     */
    public void markAsAnswered() {
        long qid = Long.parseLong(upvoteTextFieldId.getText());
        long nid = Long.parseLong(nidAnswerTextField.getText());
        ServerCommunication.markQuestionAsAnswered(qid, nid);
    }

    /**
     * Create room with room name (from text field), and whether the room is open (from checkbox).
     */
    public void createRoom() {
        String name = createRoomName.getText();
        String modPassword = modPasswordTextField.getText();
        boolean open = createRoomOpen.isSelected();
        ServerCommunication.createRoom(new Room(name, modPassword, open, null, -1, -1));
    }

    /**
     * Gets all rooms.
     */
    public void getRooms() {
        List<Room> rooms = ServerCommunication.getRooms();
        questionList.getItems().clear();
        questionList.getItems().addAll(rooms);
    }

    /**
     * Delete a room with a certain id (from text field).
     */
    public void deleteRoom() {
        long id = Long.parseLong(deleteRoomId.getText());
        ServerCommunication.deleteRoom(id);
    }

    /**
     * Open a room with a certain id.
     */
    public void openRoom() {
        long id = Long.parseLong(openRoomId.getText());
        ServerCommunication.openRoom(id, true);
    }

    /**
     * Close a room with a certain id.
     */
    public void closeRoom() {
        long id = Long.parseLong(openRoomId.getText());
        ServerCommunication.openRoom(id, false);
    }
}
