import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private static Clip backgroundMusicClip;

    public static void playBackgroundMusic(String filePath) {
        try {
            // Load the audio file
            File musicFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);

            // Loop the music indefinitely
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);

            // Start the music in a separate thread to avoid blocking the main UI thread
            new Thread(() -> {
                backgroundMusicClip.start();
            }).start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }
}
