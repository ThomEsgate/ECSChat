package uk.ac.soton.comp1206.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.network.MessageListener;
import uk.ac.soton.comp1206.utility.Utility;

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


    private ScrollPane scroll = new ScrollPane();
    public TextFlow textFlow = new TextFlow();

    /**
     * Create a new Chat Window, linked to the main App and the Communicator
     * @param app the main app
     * @param communicator the communicator
     */
    public ChatWindow(App app, Communicator communicator) {
        this.app = app;
        this.communicator = communicator;

        this.communicator.addListener((message) -> {
            Platform.runLater(() -> {
                this.receiveMessage(message);
            });
        });

        textFlow.getChildren().add(new Text("Don't tell your password to anyone.\n"));

        scroll.setContent(textFlow);

        //Setup scene with a border pane
        var pane = new BorderPane();
        this.scene = new Scene(pane,640,480);

        //Link the communicator to this window
        //communicator.setWindow(this);

        var textInput = new TextArea();
        textInput.setMaxHeight(scene.getHeight()/16);
        var buttonSubmit = new Button("Send");

        buttonSubmit.setOnAction(e -> {sendCurrentMessage(textInput.getText()); textInput.setText(""); scroll.setVvalue(1.0);});



        //Bottom Bar
        var bottomBar = new HBox();
        buttonSubmit.setMinHeight(bottomBar.getHeight());
        //Set a max width (no scrolling Horizontally when a long message is sent)
        HBox.setHgrow(textInput, Priority.ALWAYS);
        bottomBar.getChildren().addAll(textInput, buttonSubmit);


        //Centre bar
        HBox scrollHbox = new HBox();
        scrollHbox.getChildren().add(scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        scroll.setFitToWidth(true);

        //SideBar
        SideBar sideBar = new SideBar(64.0);
        String cssSideBar = this.getClass().getResource("/chat.css").toExternalForm();
        sideBar.getStylesheets().add(cssSideBar);

        //Top bar
        var topbar = new HBox();
        var settingsButton = new Button("Settings");
        topbar.getChildren().add(settingsButton);
        settingsButton.setOnAction(e -> sideBar.toggleShow());

        //Adding each bar to the location in the gridPane
        pane.setRight(sideBar);
        pane.setCenter(scrollHbox);
        pane.setBottom(bottomBar);
        pane.setTop(topbar);

        //Set the stylesheet for this window
        String css = this.getClass().getResource("/chat.css").toExternalForm();
        scene.getStylesheets().add(css);
    }



    /**
     * Handle an incoming message from the Communicator
     * @param message The message that has been received, in the form User:Message
     */
    public void receiveMessage(String message) {
        //TODO: Handle incoming messages
    
        //Splits into username then actual message
        String[] messageArray = message.split(":", 2);

        Text t = new Text(message + "\n");
        t.setFill(Color.RED);
        textFlow.getChildren().add(t);
        scroll.setVvalue(1.0);
        System.out.println();
    }

    /**
     * Send an outgoing message from the Chatwindow
     * @param text The text of the message to send to the Communicator
     */
    private void sendCurrentMessage(String text) {
        text = text.trim();//Removes trailing/leading whitespace
        if (text.length() == 0){
            return;
        }
        if (text.charAt(0) == '~') {
            getCommand(text);
        }
        else {
            communicator.send(app.getUsername() + ": " + text);
        }
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
     * @return
     */
    public Scene getScene() {
        return scene;
    }

}
