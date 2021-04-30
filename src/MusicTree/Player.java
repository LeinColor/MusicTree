package MusicTree;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread {
	public final int EventNoteOff = 0x80;
	public final int EventNoteOn = 0x90;
	public final int EventKeyPressure = 0xA0;
	public final int EventControlChange = 0xB0;
	public final int EventProgramChange = 0xC0;
	public final int EventChannelPressure = 0xD0;
	public final int EventPitchBend = 0xE0;
	public final int SysexEvent1 = 0xF0;
	public final int SysexEvent2 = 0xF7;
	public final int MetaEvent = 0xFF;
	public final int MetaEventSequence = 0x0;
	public final int MetaEventText = 0x1;
	public final int MetaEventCopyright = 0x2;
	public final int MetaEventSequenceName = 0x3;
	public final int MetaEventInstrument = 0x4;
	public final int MetaEventLyric = 0x5;
	public final int MetaEventMarker = 0x6;
	public final int MetaEventEndOfTrack = 0x2F;
	public final int MetaEventTempo = 0x51;
	public final int MetaEventSMPTEOffset = 0x54;
	public final int MetaEventTimeSignature = 0x58;
	public final int MetaEventKeySignature = 0x59;
	
	private int eventType;
	private int startTime;
	private int deltaTime;
	private int ticksPerQuarterNotes;
	private int metaEvent;
	
	private int channel;
	private int midiChannel;
	private int noteNumber;
	private int velocity;
	private int instrument;
	private int keyPressure;
	private int channelPressure;
	private int controlNum;
	private int controlValue;
	private int pitchBend;
	
	private int tempo;
	private int numerator;
	private int denominator;
	
	private int milliseconds;
	private int seconds;
	private int minutes;
	private int hours;
	
	private int[] offset = new int[30];
	private boolean status = false;
	private double timer = 0;
	private double ticks = 0;
	private double currentTempo = 120;
	private File[] pianoSrc = new File[128];
	private File[] percSrc = new File[128];
	private AudioClip[] pianoClip = new AudioClip[128];
	private AudioClip[] percClip = new AudioClip[128]; 
	private ArrayList<ArrayList<MidiEvent>> Tracks;

	private long lastTime = System.nanoTime();
	private final double nanoticks = 60D;
	private double ns = 1000000000 / nanoticks;    
	private double delta = 0;
	
	/*public void loadSound(ArrayList<ArrayList<MidiEvent>> Track, int TPQ) throws AWTException, MalformedURLException{
		String[] keyTable = { "C", "Cs", "D", "Ds", "E", "F", "Fs", "G", "Gs", "A", "As", "B" };
		
		ticksPerQuarterNotes = TPQ;
		Tracks = Track;
		ticks = TPQ * currentTempo / 60 * 0.017;
		status = true;
		
		for (int i = 0; i < 128; i++) {
			pianoSrc[i] = new File("sounds/piano/piano_" + keyTable[i % 12] + (int) (i / 12 - 2) + ".wav");
			percSrc[i] = new File("sounds/percussion/percussion_" + keyTable[i % 12] + (int) (i / 12 - 2) + ".wav");
			pianoClip[i] = Applet.newAudioClip(pianoSrc[i].toURL());
			percClip[i] = Applet.newAudioClip(percSrc[i].toURL());
		}
	}*/
	
	public void stopSound() {
            status = false;
            timer = 0;
            for (int i = 0; i < 30; i++) {
                offset[i] = 0;
            }
	}
        
        public void pauseSound() {
            status = false;
        }
        
        public void playSound() {
            status = true;
        }
	
	public void updateCurrentOffsetData() {
		eventType = Tracks.get(channel).get(offset[channel]).eventType;
		startTime = Tracks.get(channel).get(offset[channel]).startTime;
		deltaTime = Tracks.get(channel).get(offset[channel]).deltaTime;
	}
	
	public int getMilliseconds() {
		return milliseconds;
	}
	
	@Override
	public void run() {
            while (true) {
                play();
            }
	}
        
        public void play() {
            if (status) {
                channel = 9;
                currentTempo = 107;
                long now = System.nanoTime();
                long memLastTime = lastTime;
                delta += (now - lastTime) / ns;
                lastTime = now;

                if(delta >= 1) {
                timer += ticks;
                milliseconds++;
                
                        ticks = ticksPerQuarterNotes * currentTempo / 60 * ((now - memLastTime) / ns / 60);

                        updateCurrentOffsetData();

                        if (timer > startTime && offset[channel] < Tracks.get(channel).size() - 1) {
                                if(eventType == EventNoteOn && velocity != 0) {
                                        int key = noteNumber;
                                }
                        offset[channel]++;
                        }
                delta--;

                try {
                        Thread.sleep(17);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
            }
         }
    }
}