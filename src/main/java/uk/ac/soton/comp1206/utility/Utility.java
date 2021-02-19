package uk.ac.soton.comp1206.utility;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class for quick and handy static functions
 *
 * We will be adding to this later, but you can add things that are handy here too!
 */


public class Utility {

    public static SimpleBooleanProperty audioEnabledProperty = new SimpleBooleanProperty(true);
    public static MediaPlayer mediaPlayer;
    private static double lastSoundPercentage = 1.0;

    private static final Logger logger = LogManager.getLogger(Utility.class);
    private static double currentVolume = 1.0; //Current volume of media player, needed if one is destroyed and needs to be replaced at the same volume


    public static void playAudio(String file) {
        if (!audioEnabledProperty.get()) return;

        String toPlay = Utility.class.getResource("/" + file).toExternalForm();
        logger.info("Playing audio: " + toPlay);

        try {
            Media play = new Media(toPlay);
            mediaPlayer = new MediaPlayer(play);
            mediaPlayer.setVolume(currentVolume);
            mediaPlayer.play();
        } catch (Exception e) {
            setAudioEnabled(false);
            e.printStackTrace();
            logger.error("Unable to play audio file, disabling audio");
        }
    }

    //lastSoundpercentage is the percentage that the sound was at before it was muted
    public static void setLastSoundPercentage(double lastSoundPercentage) {
        Utility.lastSoundPercentage = lastSoundPercentage;
    }

    /**
     * Returns the % volume that the player was on before it was muted
     * @return lastSoundPercentage
     */
    public static double getLastSoundPercentage(){
        return lastSoundPercentage;
    }

    public static void setAudioEnabled(Boolean bool){
        audioEnabledProperty.set(bool);
    }

    public static boolean getAudioEnabled(){return audioEnabledProperty.get();}

    public static void stopAudio(){
        mediaPlayer.dispose();
    }

    public static void setVolume(double v){
        mediaPlayer.setVolume(v);
        currentVolume = v;
    }

    public static SimpleBooleanProperty audioEnabledProperty() {
        return audioEnabledProperty;
    }
}
