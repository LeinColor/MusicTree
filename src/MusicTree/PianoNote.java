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
 * PianoNote는 PianoRollScreen에서 그리는 초록색 노트 객체이다.
 * 
 * @author 12151430 이건영
 */
public class PianoNote extends JPanel {
    private Color borderColor;
    private Color backgroundColor;
    
    private int startpos = 0;
    private int endpos = 0;
    private int x = 0, y = 0;
    private int len = 0;
    private int idx = 0;
    
    private boolean isClicked = false;
    
    PianoNote() {
        this.setLayout(null);
        this.setBackground(new Color(0, 0, 0));
    }
    
    public void setIdx(int idx) {
        this.idx = idx;
    }
    
    public int getIdx() {
        return idx;
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
    
    public boolean getIsClicked() {
        return isClicked;
    }
    
    public void setIsClicked(boolean flag) {
        this.isClicked = flag;
        
        if (flag) {
            this.setBackground(new Color(255, 255, 255));
        } else {
            this.setBackground(new Color(172, 220, 181, 180));
        }
    }
    
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setEndPos(int endtime) {
        this.endpos = endtime;
    }
    
    public int getEndPos() {
        return endpos;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}