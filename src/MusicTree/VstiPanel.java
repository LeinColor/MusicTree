/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * MusicTree Synthesizer이다. 좌측메뉴의 Keyboard를 누르면 JFrame이 활성화된다.
 * 
 * @author 12151430 이건영
 */
public class VstiPanel extends JFrame implements MouseListener, Runnable{
    private final int w = 19;
    private final int h = 64;
    
    private JLayeredPane JLayeredPane = new JLayeredPane();
    private JPanel instPanel = new JPanel();
    private JLabel instrument = new JLabel();
    private JLabel[] keys = new JLabel[60];
    
    private Image whiteKey, whiteKey2;
    private Image blackKey, blackKey2;
    private boolean[] keyType = new boolean[60]; // false : whiteKey, true : blackKey
    private boolean isClicked = false;
    
    private URL[] url = new URL[60];
    private WavData[] WavData = new WavData[60];
    private int bwLen = 0;
    
    private int channel;
    private int notenumber;
    private int currentTempo;
    private boolean status = false;
    private double timer = 0;
    private double ticks = 0;
    private ArrayList<ArrayList<MidiEvent>> Tracks;

    private long lastTime = System.nanoTime();
    private final double nanoticks = 60D;
    private double ns = 1000000000 / nanoticks;    
    private double delta = 0;
    private int milliseconds;
    private int eventType;
    private int startTime;
    private int deltaTime;
    private int ticksPerQuarterNotes;
    private int metaEvent;
    private int offset;
    
    private boolean isPlaying = false;
    
    VstiPanel() {
        this.setTitle("MusicTree Synthesizer");
        this.getContentPane().setBackground(new Color(0, 0, 0));
        this.setAlwaysOnTop(true);
        this.setState(this.NORMAL);
        this.setLocation(1920 / 2 - this.getWidth(), 1080 / 2 - this.getHeight());
        this.setSize(687, 190);
        this.addMouseListener(this);
        
        this.add(JLayeredPane);
        JLayeredPane.setSize(687, 190);
        JLayeredPane.add(instPanel, 1);
        instPanel.add(instrument, BorderLayout.WEST);
        instPanel.setSize(687, 30);
        instPanel.setBackground(new Color(32, 41, 46));
        instPanel.setBorder(new LineBorder(new Color(51, 60, 65), 1));
        instrument.setText("Accordion");
        
        for (int i = 0; i < keys.length; i++) {
            url[i] = this.getClass().getResource("sounds/accordion/accordion" + (i + 1) + ".wav");
            WavData[i] = new WavData(url[i]);
            WavData[i].loadWav();
        }
        
        this.setVisible(true);
    }
    
    public void setIcon() throws IOException {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/keyboardIcon.png")));
        whiteKey = ImageIO.read(getClass().getResource("resources/whiteKey.png"));
        whiteKey2 = ImageIO.read(getClass().getResource("resources/whiteKey2.png"));
        blackKey = ImageIO.read(getClass().getResource("resources/blackKey.png"));
        blackKey2 = ImageIO.read(getClass().getResource("resources/blackKey2.png"));
        
        instrument.setFont(new Font("Tahoma", Font.PLAIN, 14));
        instrument.setForeground(new Color(1, 209, 255));
        
        
        
        for (int i = 0; i < keys.length; i++) {
            int m = i % 12;
           
            if ( (m == 0 || m == 2 || m == 4 || m == 5 || m == 7 || m == 9 || m == 11) ) {
                keys[i] = new JLabel();
                keys[i].setIcon(new ImageIcon(whiteKey));
                keys[i].setSize(w, h);
                keys[i].setLocation(bwLen, 50);
                keys[i].addMouseListener(this);
                keyType[i] = false;
                JLayeredPane.add(keys[i], 0, -1);
                
                bwLen = bwLen + w;
            }
        }
        
        bwLen = 13;
        
        for (int i = 0; i < keys.length; i++) {
            int m = i % 12;
            
            if ( (m == 1 || m == 3 || m == 6 || m == 8 || m == 10) ) {
                keys[i] = new JLabel();
                keys[i].setIcon(new ImageIcon(blackKey));
                keys[i].setSize(11, 43);
                keys[i].setLocation(bwLen, 50);
                keys[i].addMouseListener(this);
                keyType[i] = true;
                JLayeredPane.add(keys[i], 1, -1);
                
                bwLen = bwLen + w;
            } else if ( (m == 5 || m == 11) ) {
                bwLen = bwLen + w;
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
        int idx = 0;
        isClicked = true;
        
        for (int i = 0; i < keys.length; i++) {
            if (e.getSource() == keys[i]) {
                WavData[i].playChunk();
                if (!keyType[i]) {
                    keys[i].setIcon(new ImageIcon(whiteKey2));
                } else {
                    keys[i].setIcon(new ImageIcon(blackKey2));
                }
            }
        }
    }
     
    public void mouseReleased(MouseEvent e) {
        if (!isPlaying) {
        if (isClicked) {
            for (int i = 0; i < keys.length; i++) {
                WavData[i].stopChunk2();
                if(!keyType[i]) {
                    keys[i].setIcon(new ImageIcon(whiteKey));
                } else {
                    keys[i].setIcon(new ImageIcon(blackKey));
                }
            }
            isClicked = false;
        }
        }
    }

    public void mouseEntered(MouseEvent e) {
        if (isClicked) {
            for (int i = 0; i < keys.length; i++) {
                if (e.getSource() == keys[i]) {
                    WavData[i].playChunk();
                    if (!keyType[i]) {
                        keys[i].setIcon(new ImageIcon(whiteKey2));
                    } else {
                        keys[i].setIcon(new ImageIcon(blackKey2));
                    }
                }
            }
        }
    }

    public void mouseExited(MouseEvent e) { 
        if(!isPlaying) {
        for (int i = 0; i < keys.length; i++) {
            if (e.getSource() == keys[i]) {
                WavData[i].stopChunk2();
                if (!keyType[i]) {
                    keys[i].setIcon(new ImageIcon(whiteKey));
                } else {
                    keys[i].setIcon(new ImageIcon(blackKey));
                }
            }
        }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void run() {
        while (true) {
            Play();
        }
    }
    
    public void setTracks(ArrayList<ArrayList<MidiEvent>> Tracks) {
        this.Tracks = Tracks;
    }
    
    public void updateCurrentOffsetData() {
        startTime = Tracks.get(channel).get(offset).startTime;
        deltaTime = Tracks.get(channel).get(offset).deltaTime;
        eventType = Tracks.get(channel).get(offset).eventType;
    }
    
    public void Play() {
                isPlaying = true;
                channel = 10;
                currentTempo = 107;
                ticksPerQuarterNotes = 96;
                long now = System.nanoTime();
                long memLastTime = lastTime;
                delta += (now - lastTime) / ns;
                lastTime = now;

                if(delta >= 1) {
                timer += ticks;
                milliseconds++;
                
                        ticks = ticksPerQuarterNotes * currentTempo / 60 * ((now - memLastTime) / ns / 60);

                      //  for(int i = 0; i < 15; i++) {
                      //  System.out.println(Tracks.get(i).size());
                      //  }
                        //System.out.println("Instrument is " + MidiFile.Instruments[Tracks.get(channel).get(0).instrument]);
                        updateCurrentOffsetData();

                        System.out.println("offset : " + offset);
                        System.out.println("Timer : " + timer);
                        if (timer > startTime && offset < Tracks.get(channel).size() - 1) {
                                if(eventType == MidiFile.EventNoteOn && Tracks.get(channel).get(offset).velocity != 0) {
                                        notenumber = Tracks.get(channel).get(offset).noteNumber - 36;
                                        System.out.println("ggggafgfsgfs");
                                        pressShow();
                                } else if( (eventType == MidiFile.EventNoteOn && Tracks.get(channel).get(offset).velocity != 0 ) ||
                                           (eventType == MidiFile.EventNoteOff)) {
                                    notenumber = Tracks.get(channel).get(offset).noteNumber - 36;
                                    releaseShow();
                        }
                        offset++;
                        }
                delta--;

                try {
                        Thread.sleep(17);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
            }
         }
    
    public void pressShow() {
        if ( (notenumber >= 0 && notenumber < 60) ) {
            if (!keyType[notenumber]) {
                keys[notenumber].setIcon(new ImageIcon(whiteKey2));
              //  keys[notenumber].repaint();
            } else {
                keys[notenumber].setIcon(new ImageIcon(blackKey2));
               // keys[notenumber].repaint();
            }
        }
    }
    
    public void releaseShow() {
        if ( (notenumber >= 0 && notenumber < 60) ) {
            if (!keyType[notenumber]) {
                keys[notenumber].setIcon(new ImageIcon(whiteKey));
                keys[notenumber].repaint();
            } else {
                keys[notenumber].setIcon(new ImageIcon(blackKey));
                keys[notenumber].repaint();
            }
        }
    }
}