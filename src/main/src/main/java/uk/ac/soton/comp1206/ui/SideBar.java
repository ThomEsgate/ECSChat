package uk.ac.soton.comp1206.ui;

import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.utility.Utility;

import java.util.ArrayList;
import java.util.Random;

public class SideBar extends VBox {

    private ArrayList<Node> components = new ArrayList<>();
    private Boolean isVisible = true;
    private double width;

    public void toggleShow(){
        if (isVisible){
            isVisible = false;
            setManaged(false);
        }
        else {
            isVisible = true;
            setManaged(true);

        }
        for (Node component : components){
            component.setVisible(isVisible);
        }

    }

    public SideBar(double width){
        setPrefWidth(width);
        this.width = width;
      //String css = this.getClass().getResource("/chat.css").toExternalForm();
      //scene.getStylesheets().add(css);

        Label labelVolume = new Label("Volume");
        components.add(labelVolume);
        ScrollBar s1 = new ScrollBar();
        components.add(s1);
        Label labelVolumePercent = new Label("0%");
        components.add(labelVolumePercent);
        //labelVolumePercent.setPrefWidth(scene.getWidth()/18);
        var audioEnabled = new CheckBox("Audio");
        components.add(audioEnabled);
        audioEnabled.selectedProperty().bindBidirectional(Utility.audioEnabledProperty());

        var muteButton = new Button("Mute");
        components.add(muteButton);
        //muteButton.setPrefWidth(scene.getWidth()/12);



        muteButton.setOnAction(e -> {
            //Muting
            if (s1.getValue() > 0 || (Utility.getLastSoundPercentage() == 0.0) ){
                Utility.setLastSoundPercentage(s1.getValue());
                Utility.setVolume(0.0);
                s1.setValue(0.0);

            }//Unmuting
            else {
                Utility.setVolume(Utility.getLastSoundPercentage());
                s1.setValue(Utility.getLastSoundPercentage());
            }
        });

//        var nextSongButton = new Button("New song");
//        components.add(nextSongButton);
//        Random random = new Random();
//        nextSongButton.setOnAction( e -> {
//            Utility.stopAudio();
//            Utility.playAudio("gamestartup" + (random.nextInt(29) + 1) + ".mp3"); //plays a random mp3 from gamestartup1.mp3 to gamestartup29.mp3
//        });



        s1.setMin(0);
        s1.setBlockIncrement(0.01);
        s1.setMax(1);
        s1.setOrientation(Orientation.HORIZONTAL);
        Utility.setVolume(s1.getValue());


        //getChildren().addAll(labelVolume, s1, labelVolumePercent, muteButton, nextSongButton, audioEnabled);

        //toggleShow();//hide it initially
    }

}
