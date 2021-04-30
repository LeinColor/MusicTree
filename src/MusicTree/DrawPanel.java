/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import static java.util.Optional.empty;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * ProjectPanel에서 Draw모드로 드래그할 때 그려지는 사각형이다.
 * 사각형을 그리고 나서 PianoRoll화면에서 Note를 그리면 사각형 안에 분홍색 미니노트가 그려진다(NotePreview 메소드)
 * 
 * isClicked 변수는 해당 DrawPanel이 클릭되었는지 체크하기 위해 존재하는 플래그이다.
 * 
 * @author 12151430 이건영
 */
public class DrawPanel extends JPanel {
    private NotePreviewed[] Note;
    private int w = 2000;
    private int h = 40;
    private Color borderColor;
    private Color backgroundColor;
    
    private boolean isClicked = false;
    private boolean isModified = false;
    
    DrawPanel() {
        this.setLayout(null);
        this.setBackground(new Color(0, 0, 0));
    }
    
    public void setBorder() {
        this.borderColor = new Color(33, 49, 59);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, borderColor));
        this.repaint();
    }
    
    public void setColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.repaint();
    }
    
    public void setBackgroundColor() {
        this.setBackground(backgroundColor);
        this.repaint();
    }
    
    public int getW() {
        return w;
    }
    
    public boolean getIsClicked() {
        return isClicked;
    }
    
    public void setIsClicked(boolean flag) {
        this.isClicked = flag;
        
        if (flag) {
            this.setBackground(new Color(255, 255, 255));
        } else {
            this.setBackground(new Color(1, 209, 255, 120));
        }
    }
    
    public void setIsClicked(boolean flag, Color c) {
        this.isClicked = flag;
        
        if (flag) {
            this.setBackground(new Color(255, 255, 255));
        } else {
            this.setBackground(c);
        }
    }
    
    public void redraw() {
        for (int i = 0; i < Note.length; i++) {
            Note[i].setVisible(false);
            Note[i].remove(this);
    //        Note[i].repaint();
        }
    }
    
    public void drawNotePreview(ArrayList<MidiEvent> event, int ticks) {
        if (isModified)
            redraw();
        Note = new NotePreviewed[event.size()];
        
        int drawbegin = 0;
        int starttime = 0;
        int endtime = 0;
        int notenumber = 0;
        int x1 = 0;
        int x2 = 0;
        double tmp;
        
        for (int i = 0; i < event.size(); i++) {
            if (event.get(i).eventType == MidiFile.EventNoteOn && event.get(i).velocity > 0) {
                tmp = (double)event.get(i).startTime / ticks * BarPanel.w;
                drawbegin = (int)tmp;
                break;
            }
        }
        
        for (int i = 0; i < event.size(); i++) {
            Note[i] = new NotePreviewed();
            if (event.get(i).eventType == 0x90 && event.get(i).velocity > 0) {
                starttime = event.get(i).startTime;
                tmp = (double)starttime / ticks * BarPanel.w;
                x1 = (int)tmp;
                
                for (int j = i; j < event.size(); j++) {
                    if ( (event.get(j).eventType == MidiFile.EventNoteOn && event.get(j).velocity == 0 ) ||
                          event.get(j).eventType == MidiFile.EventNoteOff) {
                        endtime = event.get(j).startTime;
                        tmp = (double)endtime / ticks * BarPanel.w;
                        x2 = (int)tmp;
                        break;
                    }
                }
                
                notenumber= event.get(i).noteNumber;
                
                Note[i].setW(x2 - x1 + 1);
                Note[i].setSize(150, 100);
                tmp = TrackPanel.HEIGHT - (TrackPanel.HEIGHT * ((double)notenumber / 128));
                Note[i].setLocation(x1 - drawbegin, (int)tmp);
                
                this.add(Note[i]);
                this.revalidate();
                this.repaint();
            }
        }
        
        isModified = true;
    }
}