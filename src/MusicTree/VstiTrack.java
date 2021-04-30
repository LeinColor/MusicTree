/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.net.URL;
import java.util.ArrayList;
import javafx.scene.media.Media;

/**
 * 각 트랙의 MidiEvent를 하나씩 재생시키는 스레드이다
 * 
 * @author 12151430 이건영
 */
public class VstiTrack implements Runnable {
    public WavData[] wavData = new WavData[60];
    public URL[] url = new URL[60];
    private boolean isPlaying = false;
    
    private final String[] scale = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
    private int channel;
    private int notenumber;
    private int instrument = 0;
    private double ticks = 0;
    private int ticksPerQuarterNotes = 256;
    private int tempo = 120;
    private int eventType;
    private int startTime;
    private int offset;
    
    private double timer;
    private long lastTime = System.nanoTime();
    private final double nanoticks = 60D;
    private double ns = 1000000000 / nanoticks;    
    private double delta = 0;
    private ArrayList<MidiEvent> Track;
    private ArrayList<VstiTrack> VVstiTrack;
    private UpdateOffset UpdateOffset;
    
    VstiTrack() {
        for (int i = 0; i < url.length; i++) {
            url[i] = this.getClass().getResource("sounds/piano/piano_" + scale[i % 12] + "" + ( (i / 12) + 1 ) + ".wav");
            System.out.println(url[i]);
            wavData[i] = new WavData(url[i]);
            wavData[i].loadWav();
        }
    }
    
    public void setUpdateOffset(UpdateOffset UpdateOffset) {
        this.UpdateOffset = UpdateOffset;
    }
    
    public void setTicks(double ticks) {
        this.ticks = ticks;
    }
    
    public void setVVstiTrack(ArrayList<VstiTrack> VVstiTrack) {
        this.VVstiTrack = VVstiTrack;
    }
    
    public void setVstiTrack(ArrayList<MidiEvent> Track) {
        this.Track = Track;
        for (int i = 0; i < Track.size(); i++) {
            System.out.println(Track.get(i).noteNumber);
        }
    }
            
    public void updateCurrentOffsetData() {
        startTime = Track.get(offset).startTime;
        eventType = Track.get(offset).eventType;
    }
    
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    
    public void setTicksPerQuarterNotes(int ticks) {
        this.ticksPerQuarterNotes = ticks;
    }
    
    public void resetTimer() {
        offset = 0;
        timer = 0;
    }
    
    public void setTimer(double timer) {
        this.timer = timer;
    }
    
    public void run() {
        Play();
    }
    
    public void Play() {
        timer = 0;
        lastTime = System.nanoTime();
        while (true) {
            isPlaying = true;
            channel = 0;
            long now = System.nanoTime();
            long memLastTime = lastTime;
            delta += (now - lastTime) / ns;
            lastTime = now;

          //  if(delta >= 1) {
   //         System.out.println("offset : " + offset);
     //       System.out.println("Timer : " + timer);
            if (timer > startTime && offset < Track.size() - 1) {
                updateCurrentOffsetData();
                
                    if (eventType == MidiFile.EventNoteOn && Track.get(offset).velocity != 0) {
                            notenumber = Track.get(offset).noteNumber;
                            if ( (notenumber >= 0 && notenumber < 60) && Track.get(offset).channel != 9)
                                wavData[notenumber].playWav();

                            System.out.println("ggggafgfsgfs");
                    } if ( (eventType == MidiFile.EventNoteOn && Track.get(offset).velocity == 0 ) ||
                                (eventType == MidiFile.EventNoteOff) ) {
                        notenumber = Track.get(offset).noteNumber;
                        if ( (notenumber >= 0 && notenumber < 60) && Track.get(offset).channel != 9) {
                            //wavData[notenumber].stopChunk2();
                        }
                    }
                    
                    if ( Track.get(offset).tempo > 0 ) {
                        this.tempo = Track.get(offset).tempo;
                        UpdateOffset.setTempo(tempo);
                        System.out.println("Tempo Changed");
                    }
                offset++;
            } 
            delta--;

            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}
            if (offset > Track.size()) {
                break;
            }
        }
    }
}