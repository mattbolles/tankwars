package resource;
/**
 * Based off of resource.SoundPlayer from Airstrike game by Anthony Souza.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    private AudioInputStream soundStream;
    private String soundFile;
    private boolean started;
    private Clip clip;
    private int type;//1 for sounds that needs to be played all the time
    // 2 for sounds that only need to be played once


    public SoundPlayer(int type, String soundFile, boolean started){
        this.soundFile = soundFile;
        this.type = type;
        this.started = started;
        try{
            soundStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getClassLoader().getResource(soundFile));
            clip = AudioSystem.getClip();
            clip.open(soundStream);
        }
        catch(Exception e){
            System.out.println(e.getMessage() + "No sound documents are found");
        }
        if(this.type == 1){
            Runnable myRunnable = () -> {
                if (started) {
                    try {
                    while (true) {
                        clip.start();
                        clip.loop(clip.LOOP_CONTINUOUSLY);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    } catch (Exception ex) {
                        Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public void play(){
        clip.start();
    }
    public void stop(){
        clip.stop();
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
