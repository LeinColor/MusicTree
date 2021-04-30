/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 * PianoNote 객체를 그리는 화면이다.
 * MIDI 파일을 열면 트랙마다 모든 음을 다 그려서 불러온다.
 * 그리기, 지우기, 방향키로 y좌표 조절 등이 전부 담겨있다.
 * 
 * @author 12151430 이건영
 */
public class PianoRollScreen extends JLayeredPane implements MouseListener, MouseMotionListener, KeyEventDispatcher {
    private PRBarPanel[] bp;
    private BarRowLine[] br;
    private int memX;
    private int memY;
    private int memNotenumber;
    private int ticksAtCurrentPos;
    private int memIdx = -1;
    private int selectIdx = -1;
    private VstiTrack VstiTrack;
    private double tmp;
    private int x1, x2;
    
    private WavData[] wavData = new WavData[60];
    private URL[] url = new URL[60];
    
    private int ticks = 256;        // quarter note (4th note)
    private ArrayList<PianoNote> Notes = new ArrayList<PianoNote>();
    private ArrayList<MidiEvent> Track = new ArrayList<MidiEvent>();
    private KeyboardFocusManager manager;
    
    private boolean drawMousePressed = false;
    private boolean selectMousePressed = false;
    private int selectMousePressed2 = 0;
    PianoRollScreen() {
        int barChunkCount = 800;
        
        bp = new PRBarPanel[barChunkCount];
        for (int i = 0; i < barChunkCount; i++) {
            bp[i] = new PRBarPanel();

            if (i % 4 == 0) {
                bp[i].setBoldBorder();
            } else {
                bp[i].setNormalBorder();
            }

            if ((i % 32) < 16) {
                bp[i].setNormalBackground();
            } else {
                bp[i].setBoldBackground();
            }

            bp[i].setXPos((PRBarPanel.w * i ));
            bp[i].setLocation((PRBarPanel.w * i), 0);
            this.add(bp[i], 0, -1);
        }

        br = new BarRowLine[200];
        for (int i = 0; i < 200; i++) {
            br[i] = new BarRowLine();
            br[i].setLocation(0, ((PianoRollKey.HEIGHT) * i - 1));
            this.add(br[i], 2, -1);
        }
        
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public void setPianoRollTrack(ArrayList<MidiEvent> Track, ArrayList<PianoNote> Notes) {
        this.Track = Track;
        this.Notes = Notes;
      /*  for (int i = 0; i < Track.size(); i++) {
            if ( (Track.get(i).eventType != MidiFile.EventNoteOn && Track.get(i).eventType != MidiFile.EventNoteOff ) ||
                  (Track.get(i).noteNumber == 0 && Track.get(i).velocity == 0) ) {
                Track.remove(i);
            }
        }
        for (int i = 0; i < Track.size(); i++) {
            if ( (Track.get(i).eventType != MidiFile.EventNoteOn && Track.get(i).eventType != MidiFile.EventNoteOff ) ||
                  (Track.get(i).noteNumber == 0 && Track.get(i).velocity == 0) ) {
                Track.remove(i);
                i = 0;
            }
        }*/
    }
    
    public void setVstiTrack(VstiTrack VstiTrack) {
        this.VstiTrack = VstiTrack;
    }
    
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
    
    public void loadNotes() {
        for (int i = 0; i < Track.size(); i++) {
            if (Track.get(i).eventType == MidiFile.EventNoteOn && Track.get(i).velocity > 0) {
                PianoNote note = new PianoNote();
                Notes.add(note);
                note.setBorder();
                note.setColor(new Color(172, 220, 181, 180));
                note.setBackgroundColor();
                note.addMouseListener(this);

                memIdx++;
                tmp = (double)Track.get(i).startTime / ticks * 128;
                x1 = (int)tmp;
                note.setPos(x1 + 1, (127 - (Track.get(i).noteNumber )) * PianoRollKey.HEIGHT);
                note.setIdx(memIdx);
                note.setLocation(x1 + 1, (127 - (Track.get(i).noteNumber )) * PianoRollKey.HEIGHT);
                for (int j = i; j < Track.size(); j++) {
                    if ( (Track.get(j).eventType == MidiFile.EventNoteOn && Track.get(j).velocity == 0 ) ||
                          Track.get(j).eventType == MidiFile.EventNoteOff) {
                        tmp = (double)Track.get(j).startTime / ticks * 128;
                        x2 = (int)tmp;
                        break;
                    }
                }
                note.setSize(x2 - x1 - 1, PianoRollKey.HEIGHT);
                note.setVisible(false);
                System.out.println(i + "th Load : " + note);
                this.add(note, 1, -1);
            }
        }
    }
    
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == this && !drawMousePressed && Graphic.TOOL == Graphic.TOOL.DRAW) {
            PianoNote note = new PianoNote();
            MidiEvent startEvent = new MidiEvent();
            Notes.add(note);
            Track.add(startEvent);
            note.setSize(32, PianoRollKey.HEIGHT);
            ticksAtCurrentPos = (ticks / 8) * ( 32 / 32 );
            
            note.setBorder();
            note.setColor(new Color(255, 255, 255, 120));
            note.setBackgroundColor();
            note.addMouseListener(this);
            drawMousePressed = true;
            memX = 32 * (e.getX() / 32) + 1;
            memY = (PianoRollKey.HEIGHT) * (e.getY() / PianoRollKey.HEIGHT);
            VstiTrack.wavData[127 - (e.getY() / PianoRollKey.HEIGHT)].playWav();
            startEvent.startTime = (ticks / 8) * (e.getX() / 32);
            startEvent.eventType = MidiFile.EventNoteOn;
            startEvent.channel = 0;
            startEvent.velocity = 127;
            startEvent.noteNumber = 127 - (e.getY() / PianoRollKey.HEIGHT);
            memNotenumber = startEvent.noteNumber;
            
            memIdx++;
            Notes.get(memIdx).setEndPos(32);
            note.setPos(memX, memY);
            note.setIdx(memIdx);
            note.setLocation(memX, memY);
            this.add(note, 1, -1);
        }
        
        if (e.getButton() == MouseEvent.BUTTON1 && Notes.size() > 0 && Graphic.TOOL == Graphic.TOOL.SELECT) {
            selectIdx = -1;

            for (int i = 0; i < Notes.size(); i++) {
                if (e.getSource() == Notes.get(i)) {
                    selectMousePressed2 = 1;
                    selectIdx = i;
               }
           }

          for (int i = 0; i < Notes.size(); i++) {
              if (Notes.get(i).getIsClicked()) {
                   Notes.get(i).setIsClicked(false);
                   Notes.get(i).repaint();
              }
          }
          this.repaint();
          
          if (selectIdx >= 0) {
            selectMousePressed2 = 1;
            Notes.get(selectIdx).setIsClicked(true);
            Notes.get(selectIdx).repaint();
            if (Track.get(selectIdx).noteNumber >= 0 && Track.get(selectIdx).noteNumber < 60) {
                VstiTrack.wavData[Track.get(selectIdx * 2).noteNumber].playWav();
            }
            this.repaint();
          }
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if (drawMousePressed) {
            MidiEvent endEvent = new MidiEvent();
            drawMousePressed = false;
           
            endEvent.startTime = ticksAtCurrentPos;
            endEvent.eventType = MidiFile.EventNoteOn;
            endEvent.channel = 0;
            endEvent.velocity = 0;
            endEvent.noteNumber = memNotenumber;
            Track.add(endEvent);
            Notes.get(memIdx).setColor(new Color(172, 220, 181, 180));
            Notes.get(memIdx).setBackgroundColor();
            this.repaint();
        }
        
        if (selectMousePressed2 == 0) {
            selectMousePressed2 = 1;
        }
    }
    
    public void mouseEntered(MouseEvent e) {
        
    }
    
    public void mouseExited(MouseEvent e) {
        
    }
    
    public void mouseClicked(MouseEvent e) {
        
    }
    
    public void mouseMoved(MouseEvent e) {

    }
    
    public void mouseDragged(MouseEvent e) {
        if (drawMousePressed) {
            Notes.get(memIdx).setSize( 32 * ( (e.getX() - memX + 32) / 32 ) - 1, PianoRollKey.HEIGHT);
            Notes.get(memIdx).setEndPos( 32 * ( (e.getX() + 32) / 32) );
            ticksAtCurrentPos = (ticks / 8) * ( (e.getX() + 32) / 32 );
        }
    }
    
     @Override
    public boolean dispatchKeyEvent(KeyEvent e)
    {
        if (!e.isConsumed())
        {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE && Notes.size() > 0 && Graphic.TOOL == Graphic.TOOL.SELECT && selectIdx >= 0)
                {
                    Notes.get(selectIdx).setVisible(false);
                    Notes.get(selectIdx).remove(this);
                    Notes.remove(selectIdx);
                    Track.remove(selectIdx * 2);
                    Track.remove(selectIdx * 2);
                    memIdx--;
                    selectIdx = 0;
                    System.out.println(selectIdx + " is deleted");
                    e.consume();
                    return true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP && Notes.size() > 0 && Graphic.TOOL == Graphic.TOOL.SELECT && selectIdx >= 0) {
                    Notes.get(selectIdx).setLocation( Notes.get(selectIdx).getX(), Notes.get(selectIdx).getY() - PianoRollKey.HEIGHT);
                    Notes.get(selectIdx).setY(Notes.get(selectIdx).getY() - PianoRollKey.HEIGHT);
                    Track.get(selectIdx * 2).noteNumber += 1;
                    Track.get(selectIdx * 2 + 1).noteNumber += 1;
                    if (Track.get(selectIdx).noteNumber >= 0 && Track.get(selectIdx).noteNumber < 60) {
                        VstiTrack.wavData[Track.get(selectIdx * 2).noteNumber].playWav();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && Notes.size() > 0 && Graphic.TOOL == Graphic.TOOL.SELECT && selectIdx >= 0) {
                    Notes.get(selectIdx).setLocation( Notes.get(selectIdx).getX(), Notes.get(selectIdx).getY() + PianoRollKey.HEIGHT);
                    Notes.get(selectIdx).setY(Notes.get(selectIdx).getY() + PianoRollKey.HEIGHT);
                    Track.get(selectIdx * 2).noteNumber -= 1;
                    Track.get(selectIdx * 2 + 1).noteNumber -= 1;
                    if (Track.get(selectIdx).noteNumber >= 0 && Track.get(selectIdx).noteNumber < 60) {
                        VstiTrack.wavData[Track.get(selectIdx * 2).noteNumber].playWav();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    for (int i = 0; i < Track.size(); i++) {
                        System.out.println("==============" + i + "th Track Info " + "============" + "\n" +
                                            Track.get(i).startTime + "\n" +
                                            Track.get(i).velocity + "\n" +
                                            Track.get(i).noteNumber + "\ntick : " +
                                            ticks );
                    }
                }
            }
        }
        return false;
    }
    
    public ArrayList<MidiEvent> getTrack() {
        return Track;
    }
    
    public void showNotes() {
        memIdx = Notes.size() - 1;
        for (int i = 0; i < Notes.size(); i++) {
            Notes.get(i).setVisible(true);
            System.out.println(i + "th Notes : " + Notes.get(i));
           // System.out.println(Track);
        }
    }
    
    public void hideNotes() {
        for (int i = 0; i < Notes.size(); i++) {
            Notes.get(i).setVisible(false);
        }
    }
}