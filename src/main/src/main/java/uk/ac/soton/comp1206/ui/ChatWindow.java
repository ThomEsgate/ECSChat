package uk.ac.soton.comp1206.ui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.network.MessageListener;
import uk.ac.soton.comp1206.utility.Utility;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Chat window which will display chat messages and a way to send new messages
 */
public class ChatWindow {

    private static final Logger logger = LogManager.getLogger(ChatWindow.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private final App app;
    private final Scene scene;
    private final Communicator communicator;

    private Whiteboard whiteboard;

    //For FXML, from LoginWindow.java
    Parent root = null;

    //topBar
    @FXML
    Button closeButton;
    @FXML
    Button minimiseButton;

    //topBar1
    @FXML
    Button settingsButton;

    //centrePane
    @FXML
    ScrollPane scroll;
    @FXML
    TextFlow textFlow;

    //sideBar
    @FXML
    VBox settingsVBox;
    @FXML
    Label mainSettingsLabel;
    @FXML
    Label settingsTypeLabel;
    @FXML
    Label settingsTypeLabel1;
    @FXML
    ScrollBar s1;
    @FXML
    Label volumeLabel;
    @FXML
    Label volumeLabelPercent;
    @FXML
    CheckBox audioEnabled;
    @FXML
    Button settingsBackButton;

    //bottomBar
    @FXML
    TextArea textInput;
    @FXML
    Button buttonSubmit;


    /**
     * Create a new Chat Window, linked to the main App and the Communicator
     * @param app the main app
     * @param communicator the communicator
     */
    public ChatWindow(App app, Communicator communicator) {
        this.app = app;
        this.communicator = communicator;

        //Load the Chat Window GUI
        try {
            //Instead of building this GUI programmatically, we are going to use FXML
            var loader = new FXMLLoader(getClass().getResource("/chat.fxml"));

            //Link the GUI in the FXML to this class
            loader.setController(this);
            root = loader.load();

            //Close the settings bar initially
            toggleSettings();
        } catch (Exception e) {
            //Handle any exceptions with loading the FXML
            logger.error("Unable to read file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        this.scene = new Scene(root);

        //LISTENERS ********************************************
        //Message Listener
        this.communicator.addListener((message) -> {
            Platform.runLater(() -> {
                this.receiveMessage(message);
            });
        });

        //Listener for % value under the volume bar
        s1.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            volumeLabelPercent.setText( Math.round(s1.getValue()*100) + "%");
            Utility.setVolume(s1.getValue());
        });

        String css = this.getClass().getResource("/chat.css").toExternalForm();
        scene.getStylesheets().add(css);


    }

    /**
     * Handle an incoming message from the Communicator
     * @param message The message that has been received, in the form User:Message
     */
    public void receiveMessage(String message) {
        //TODO: Handle incoming messages
        scroll.setVvalue(1.0); //Scroll to the bottom to see the new message
        //Splits into username then actual message
        String[] messageArray = message.split(":", 2);
        messageArray[0] = formatText(messageArray[0]); //Add time + formating
        Text t = new Text(messageArray[0] + ": " + messageArray[1] + "\n");
        t.setFill(Color.RED);

        Utility.playAudio("warning.wav");//Notification sound

        textFlow.getChildren().add(t);
        scroll.setVvalue(1.0);
    }

    /**
     * Send an outgoing message from the Chatwindow
     * @param
     */
    @FXML protected void sendCurrentMessage(ActionEvent event) {
        String text = textInput.getText();
        text = text.trim();//Removes trailing/leading whitespace
        if (text.length() == 0){ return;}

        //Clear chatbox
        textInput.setText("");

        if (text.charAt(0) == '~') {
            getCommand(text);
        }

        else {
            communicator.send(app.getUsername() + ": " + text);
        }
    }

    /**
     * Hide/show the whiteboard
     */
    @FXML protected void toggleWhiteboard(){
        whiteboard.setActive(!whiteboard.isActive);
    }

    /**
     * Create the whiteboard and display it
     */
    @FXML protected void createWhiteboard(){
        Whiteboard whiteboard = new Whiteboard(400,300);
        whiteboard.setMode(DrawingMode.DRAWING);
        whiteboard.showWhiteboard();

    }
    /**
     * Open/close settings box when settings button is clicked
     */
    @FXML protected void toggleSettings(){
        settingsVBox.setManaged((!settingsVBox.isManaged()));
    }

    /**
     * For spliting up messages into commands and paramaters
     */
    private void getCommand(String text){
        String param = "";
        String[] textArray = text.split(" ");
        String command = textArray[0].substring(1);
        System.err.println(command);
        if (textArray.length > 1) {
            param = textArray[1];
            System.err.println(param);
        }
        else System.err.println("No parameters given");

        if (command.equals("nick")){
            if (param.length() == 0){
                app.setUsername("Guest"); //Resets nickname with no parameter
            }
            else app.setUsername(param);
        }
        else System.err.println("Command not found: " + command);

    }

    /**
     * Get the scene contained inside the Chat Window
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

    private String formatText(String t){
        return "Ti:me " + t;
    }

}
