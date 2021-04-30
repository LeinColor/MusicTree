/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 음원파일을 담는 객체이다.
 * 
 * @author 12151430 이건영
 */
public class WavData {
    URL path;
    Clip clip;
    int currentPosition = 0;
    long millisecondLength;
    
    WavData(URL path) {
        this.path = path;
    }
    
    void loadWav() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            millisecondLength = clip.getMicrosecondLength() / 1000;
            System.out.println("========= Path : " + path);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
   /* @Override
    public void run() {
        try {
            clip.start();
            Thread.sleep( millisecondLength / 1000 );
        } catch (Exception e) { e.printStackTrace(); }
    }*/
    
    public void playWav() {
        setPosition(currentPosition);
        clip.start();
    }
    
    public void stopWav() {
        currentPosition = 0;
        clip.stop();
    }
    
    public void pauseWav() {
        currentPosition = clip.getFramePosition();
        clip.stop();
    }
    
    public void setPosition(int value) {
        clip.setFramePosition(value);
    }
    
    public long getPosition() {
        return clip.getLongFramePosition();
    }
    
    public void playChunk() {
        clip.start();
    }
    
    public void stopChunk2() {
        clip.setFramePosition(0);
        clip.stop();
    }
}
