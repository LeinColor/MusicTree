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
 * Add Instrument Track을 눌렀을 때 좌측에 생성되는 사각형 패널이다.
 * MIDI 파일을 불러왔을 때 트랙 갯수만큼 만들어 진다.
 * 
 * @author 삼성전자
 */
public class TrackPanel extends JPanel {
    private JLabel trackNameLabel = new JLabel();
    private JLabel mute = new JLabel();
    private JLabel mixer = new JLabel();
    private Color color1;
    private Color color2;
    private Image muteIcon;
    private Image mixerIcon01;
    private int slot;
    
    private int channel;
    private int instrument;
    private int tracknumber;
    private boolean isClicked = false;
    
    private final int OPACITY = 120;
    public static final int HEIGHT = 40;
    
    TrackPanel(int slot) {
        this.setLayout(new BorderLayout());
        this.slot = slot;
        
        try {
            InputStream fontStream = getClass().getResourceAsStream("fonts/Cuprum.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            font = font.deriveFont(Font.PLAIN, 14);
            fontStream.close();
            
            trackNameLabel.setFont(font);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        trackNameLabel.setForeground(new Color(210, 231, 255));
        trackNameLabel.setSize(20,20);
        trackNameLabel.setLocation(15, 10);
        this.add(trackNameLabel, BorderLayout.WEST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        
        if (slot == 0) {
            color1 = new Color(87, 141, 68);
            color2 = new Color(117, 171, 98);
        } else if (slot == 1) {
            color1 = new Color(154, 91, 62);   // 36 69 76
            color2 = new Color(184, 121, 92);  // 66 99 106
        } else if (slot == 2) {
            color1 = new Color(70, 89, 143);
            color2 = new Color(100, 119, 173);
        } else if (slot == 3) {
            color1 = new Color(61, 84, 153);
            color2 = new Color(91, 114, 183);
        } else if (slot == 4) {
            color1 = new Color(141, 70, 68);
            color2 = new Color(171, 100, 98);
        } else if (slot == 5) {
            color1 = new Color(80, 112, 66);
            color2 = new Color(110, 142, 96);
        } else if (slot == 6) {
            color1 = new Color(66, 112, 109);
            color2 = new Color(96, 142, 149);
        } else if (slot == 7) {
            color1 = new Color(95, 73, 95);
            color2 = new Color(125, 93, 125);
        }
        
        if (isClicked) {
            color1 = new Color(225, 225, 225);
            color2 = new Color(255, 255, 255);
            trackNameLabel.setForeground(new Color(0, 0, 0));
        } else {
            trackNameLabel.setForeground(new Color(210, 231, 255));
        }
       
        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
    
    public void setChannel(int channel) {
        this.channel = channel;
    }
    
    public int getChannel() {
        return channel;
    }
    
    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }
    
    public int getInstrument() {
        return instrument;
    }
    
    public void setTrackNumber(int tracknumber) {
        this.tracknumber = tracknumber;
    }
    
    public int getTrackNumber() {
        return tracknumber;
    }
    
    public void setTrackName(String name) {
        trackNameLabel.setText(name);
    }
    
    public void addMute() throws IOException {
        muteIcon = ImageIO.read(getClass().getResource("resources/Mute_off.png"));
        mixerIcon01 = ImageIO.read(getClass().getResource("resources/MixerIcon01.png"));
        mute.setIcon(new ImageIcon(muteIcon));
        mixer.setIcon(new ImageIcon(mixerIcon01));
        this.add(mixer, BorderLayout.CENTER);
        this.add(mute, BorderLayout.EAST);
        
    }
    
    public int getTrackGap() {
        return HEIGHT;
    }
    
    public boolean getIsClicked() {
        return isClicked;
    }
    
    public void setIsClicked(boolean flag) {
        this.isClicked = flag;
    }
    
    public Color getColor() {
        Color c = new Color(0, 0, 0);
        
        if (slot == 0) {
            c = new Color(117, 171, 98, OPACITY);
        } else if (slot == 1) {
            c = new Color(184, 121, 92, OPACITY);
        } else if (slot == 2) {
            c = new Color(100, 119, 173, OPACITY);
        } else if (slot == 3) {
            c = new Color(91, 114, 183, OPACITY);
        } else if (slot == 4) {
            c = new Color(171, 100, 98, OPACITY);
        } else if (slot == 5) {
            c = new Color(110, 142, 96, OPACITY);
        } else if (slot == 6) {
            c = new Color(96, 142, 149, OPACITY);
        } else if (slot == 7) {
            c = new Color(125, 93, 125, OPACITY);
        }
        
        return c;
    }
    
    public void repaintMute() {
        mute.repaint();
    }
    
    public void repaintMixer() {
        mixer.repaint();
    }
}