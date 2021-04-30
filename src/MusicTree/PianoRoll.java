/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
 * Piano Roll을 켰을 때 왼쪽에 건반들을 그리는 클래스이다.
 * 
 * @author 12151430 이건영
 */
public class PianoRoll extends JLayeredPane implements MouseListener {
    private final int w = 47;
    private final int h = 22;
    
    private PianoRollKey[] keys = new PianoRollKey[128];
    private boolean isClicked = false;
    
    private URL[] url = new URL[128];
    private WavData[] WavData = new WavData[128];
    private int bwLen = 0;
    
    PianoRoll() {
        this.addMouseListener(this);
    
      /*  for (int i = 0; i < keys.length; i++) {
            url[i] = this.getClass().getResource("sounds/accordion/accordion" + (i + 1) + ".wav");
            WavData[i] = new WavData(url[i]);
            WavData[i].loadWav();
        }
*/
        this.setOpaque(true);
        this.setBackground(new Color(63, 72, 77));
        this.setVisible(true);
    }
    
    public void setIcon() throws IOException {
        for (int i = 127; i >= 0; i--) {
            int m = i % 12;
           
            if ( (m == 0 || m == 2 || m == 4 || m == 5 || m == 7 || m == 9 || m == 11) ) {
                keys[i] = new PianoRollKey();
                keys[i].setSize(w, h);
                keys[i].setLocation(0, bwLen);
                keys[i].addMouseListener(this);
                keys[i].setKeyType(false);
                keys[i].setNoteNumber(i);
                keys[i].repaint();
                this.add(keys[i], 0, -1);
            }
            bwLen = bwLen + h;
        }
        
        bwLen = 0;
        
        for (int i = 127; i >= 0; i--) {
            int m = i % 12;
            
            if ( (m == 1 || m == 3 || m == 6 || m == 8 || m == 10) ) {
                keys[i] = new PianoRollKey();
                keys[i].setSize(w, h);
                keys[i].setLocation(0, bwLen);
                keys[i].addMouseListener(this);
                keys[i].setKeyType(true);
                keys[i].setNoteNumber(i);
                this.add(keys[i], 1, -1);
            }
            bwLen = bwLen + h;
        }
    }
    
    public void mousePressed(MouseEvent e) {
        int idx = 0;
        isClicked = true;
        
        for (int i = 0; i < keys.length; i++) {
            if (e.getSource() == keys[i]) {
                //WavData[i].playChunk();
                    keys[i].setIsClicked(true);
                    repaint();
            }
        }
    }
     
    public void mouseReleased(MouseEvent e) {
        if (isClicked) {
            for (int i = 0; i < keys.length; i++) {
                //WavData[i].stopChunk2();
                keys[i].setIsClicked(false);
                keys[i].repaint();
            }
            isClicked = false;
        }
    }

    public void mouseEntered(MouseEvent e) {
        if (isClicked) {
            for (int i = 0; i < keys.length; i++) {
                if (e.getSource() == keys[i]) {
                   // WavData[i].playChunk();
                    keys[i].setIsClicked(true);
                    keys[i].repaint();
                }
            }
        }
    }

    public void mouseExited(MouseEvent e) { 
        for (int i = 0; i < keys.length; i++) {
            if (e.getSource() == keys[i]) {
                //WavData[i].stopChunk2();
                keys[i].setIsClicked(false);
                keys[i].repaint();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    public void reset() {
        isClicked = false;
        for (int i = 0; i < keys.length; i++) {
            keys[i].setIsClicked(false);
            keys[i].addMouseListener(this);
            keys[i].repaint();
        }
    }
}