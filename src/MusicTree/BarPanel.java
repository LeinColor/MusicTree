/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * ProjectPanel과 PianoRollScreen의 세로 격자에 해당한다.
 * 세로로 매우 길다랗다. 이 클래스의 가로 길이는 DrawPanel과 PianoNote의 길이를 결정하는 격자의 최소단위이다.
 * 
 * @author 12151430 이건영
 */
public class BarPanel extends JPanel {
    public static final int w = 32;
    private int h = 5000;
    private int xPos = 0;
    private Color borderColor;
    private Color bgColor;
    
    BarPanel() {
        this.setLayout(new BorderLayout());
        this.setSize(w, h);
    }
    
    public void setNormalBorder() {
        this.borderColor = new Color(33, 49, 59);
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderColor));
        this.repaint();
    }
    
    public void setBoldBorder() {
        this.borderColor = new Color(0, 0, 0);
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, borderColor));
        this.repaint();
    }
    
    public void setNormalBackground() {
        this.bgColor = new Color(51, 67, 77);
        this.setBackground(bgColor);
        this.repaint();
    }
    
    public void setBoldBackground() {
        this.bgColor = new Color(45, 61, 71);
        this.setBackground(bgColor);
        this.repaint();
    }
    
    public void setPlayingBackground() {
        this.bgColor = new Color(76, 85, 90);
        this.setBackground(bgColor);
        this.repaint();
    }
    
    public int getW() {
        return w;
    }
    
    public void setXPos(int value) {
        this.xPos = value;
    }
    
    public int getXPos() {
        return xPos; 
    }
}
