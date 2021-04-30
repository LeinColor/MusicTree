/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * ProjectPanel과 PianoRollScreen의 가로 격자에 해당한다.
 * 가로로 매우 길다랗다. 이 객체는 TrackPanel의 세로 길이(HEIGHT) 간격으로 Graphic.java에서 배치된다.
 * 
 * @author 12151430 이건영
 */
public class BarRowLine extends JPanel {
    
    private int w = 24000;
    private int h = 2;
    private Color borderColor = new Color(0, 0, 0);
    private Color bgColor = new Color(0, 0, 0);
    
    BarRowLine() {
        this.setBorder(new LineBorder(borderColor, 1));
        this.setLayout(new BorderLayout());
        this.setBackground(bgColor);
        this.setSize(w, h);
    }
    
    public int getHeight() {
        return h;
    }
}
