/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * PianoRoll에서 건반 하나의 객체이다.
 * 
 * @author 12151430 이건영
 */
public class PianoRollKey extends JPanel {
    private JLabel scale = new JLabel();
    private Color color1;
    private Color color2;
    
    private int notenumber;
    private String[] scaleArr = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private boolean keyType = false;    // false : whiteKey, true : blackKey
    private boolean isClicked = false;
    
    public static final int HEIGHT = 22;
    
    PianoRollKey() {
        this.setLayout(new BorderLayout());
        
        try {
            InputStream fontStream = getClass().getResourceAsStream("fonts/Cuprum.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            font = font.deriveFont(Font.PLAIN, 14);
            fontStream.close();
            
            scale.setFont(font);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        scale.setSize(47, 22);
        this.add(scale, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        if (isClicked) {
            color1 = new Color(1, 209, 225);
            color2 = new Color(31, 239, 255);
            scale.setForeground(new Color(0, 0, 0));
        } else {
            if (keyType) {
                color1 = new Color(45, 49, 56);
                color2 = new Color(75, 79, 86);
                scale.setForeground(new Color(220, 220, 220));
            } else {
                if ( (notenumber % 12) != 0) {
                    color1 = new Color(180, 184, 191);
                    color2 = new Color(210, 214, 221);
                    scale.setForeground(new Color(0, 0, 0));
                } else {
                    color1 = new Color(151, 155, 162);
                    color2 = new Color(181, 185, 192);
                    scale.setForeground(new Color(0, 0, 0));
                }
            }
        }
       
        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
    
    public void setNoteNumber(int notenumber) {
        this.notenumber = notenumber;
        scale.setText(scaleArr[notenumber % 12] + "" + (notenumber / 12));
    }
    
    public int getNoteNumber() {
        return notenumber;
    }
    
    public boolean getIsClicked() {
        return isClicked;
    }
    
    public void setKeyType(boolean keyType) {
        this.keyType = keyType;
        if(!keyType) {
            scale.setForeground(new Color(0, 0, 0));
        } else {
            scale.setForeground(new Color(220, 220, 220));
        }
    }
    
    public boolean getKeyType() {
        return keyType;
    }
    
    public void setIsClicked(boolean flag) {
        this.isClicked = flag;
    }
}