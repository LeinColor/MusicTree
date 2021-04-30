/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * 분홍색 미니노트 객체이다.
 * 
 * @author 12151430 이건영
 */
public class NotePreviewed extends JComponent {
    private int w = 0;
    private int h = 2;
    
    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(218, 159, 191));
        g.fillRect(0, 0, w, h);
    }
    
    public void setW(int w) {
        this.w = w;
    }
    
    public void setH(int h) {
        this.h = h;
    }
}
