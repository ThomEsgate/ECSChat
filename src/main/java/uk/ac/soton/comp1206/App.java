package uk.ac.soton.comp1206;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.network.MessageListener;
import uk.ac.soton.comp1206.ui.ChatWindow;
import uk.ac.soton.comp1206.ui.LoginWindow;
import uk.ac.soton.comp1206.utility.Utility;

import java.util.ArrayList;
import java.util.Random;

/**
 * Our Chat application main class. This will be responsible for co-ordinating the application and handling the GUI.
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private Communicator communicator;
    private Stage stage;
    private String username;



    /**
     * Launch the JavaFX application
     * @param args
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    /**
     * Start the Java FX process - prepare and display the first window
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        communicator = new Communicator("ws://discord.ecs.soton.ac.uk:9500");

        stage.setTitle("ECS CRAP (EC)");
        stage.setOnCloseRequest(ev -> {
            shutdown();
        });
        openLogin();
    }

    /**
     * Display the login window
     */
    public void openLogin() {
        logger.info("Opening login window");
        var window = new LoginWindow(this);
        stage.setScene(window.getScene());

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Display the chat window
     */
    public void openChat() {
        logger.info("Opening chat window");
        Utility.stopAudio();//Stops the login chat music
        Random random = new Random();
        Utility.playAudio("gamestartup" + (random.nextInt(29) + 1) + ".mp3" );
        var window = new ChatWindow(this,communicator);

        stage.setScene(window.getScene());

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Shutdown the application
     */
    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    /**
     * Set the username from the login window
     * @param username
     */
    public void setUsername(String username) {
        logger.info("Username set to: " + username);
        this.username = username;
        Utility.playAudio("warning.wav");
    }

    /**
     * Get the currently logged in user name
     * @return
     */
    public String getUsername() {
        return this.username;
    }



}