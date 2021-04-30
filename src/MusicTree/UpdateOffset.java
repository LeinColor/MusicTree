/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 각 트랙의 재생되는 오프셋을 하나로 통일시켜주는 클래스이다.
 * 
 * @author 12151430 이건영
 */
public class UpdateOffset implements Runnable {
    private double ticks = 0;
    private int ticksPerQuarterNotes = 256;
    private int tempo = 120;
    private boolean isPlaying;
    private boolean musicPlay;
    
    private TimeArrow TimeArrow;
    private Image n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;

    private double timer;
    private long lastTime = System.nanoTime();
    private final double nanoticks = 60D;
    private double ns = 1000000000 / nanoticks;    
    private double delta = 0;
    private JLabel TempoDigits3, TempoDigits2, TempoDigits1;
    private ArrayList<VstiTrack> VVstiTrack;
    
    public void setVVstiTrack(ArrayList<VstiTrack> VVstiTrack) {
        this.VVstiTrack = VVstiTrack;
    }
    
    public void setTempoDigits(JLabel TempoDigits3, JLabel TempoDigits2, JLabel TempoDigits1) throws IOException {
        this.TempoDigits3 = TempoDigits3;
        this.TempoDigits2 = TempoDigits2;
        this.TempoDigits1 = TempoDigits1;
        
        n0 = ImageIO.read(getClass().getResource("resources/TimeDigits16_0.png"));
        n1 = ImageIO.read(getClass().getResource("resources/TimeDigits16_1.png"));
        n2 = ImageIO.read(getClass().getResource("resources/TimeDigits16_2.png"));
        n3 = ImageIO.read(getClass().getResource("resources/TimeDigits16_3.png"));
        n4 = ImageIO.read(getClass().getResource("resources/TimeDigits16_4.png"));
        n5 = ImageIO.read(getClass().getResource("resources/TimeDigits16_5.png"));
        n6 = ImageIO.read(getClass().getResource("resources/TimeDigits16_6.png"));
        n7 = ImageIO.read(getClass().getResource("resources/TimeDigits16_7.png"));
        n8 = ImageIO.read(getClass().getResource("resources/TimeDigits16_8.png"));
        n9 = ImageIO.read(getClass().getResource("resources/TimeDigits16_9.png"));
    }
    
    public void setTimeArrow(TimeArrow TimeArrow) {
        this.TimeArrow = TimeArrow;
    }
    
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
    
    public void setProperty(int tempo, int ticks) {
        setTempo(tempo);
        this.ticksPerQuarterNotes = ticks;
    }
    
    public void setTempo(int tempo) {
        this.tempo = tempo;
        if (this.tempo / 100 == 0) {
            TempoDigits3.setVisible(false);
        } else if (this.tempo / 100 == 1) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n1));
        } else if (this.tempo / 100 == 2) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n2));
        } else if (this.tempo / 100 == 3) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n3));
        } else if (this.tempo / 100 == 4) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n4));
        } else if (this.tempo / 100 == 5) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n5));
        } else if (this.tempo / 100 == 6) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n6));
        } else if (this.tempo / 100 == 7) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n7));
        } else if (this.tempo / 100 == 8) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n8));
        } else if (this.tempo / 100 == 9) {
            TempoDigits3.setVisible(true);
            TempoDigits3.setIcon(new ImageIcon(n9));
        }
        
        if (this.tempo % 100 / 10 == 0) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n0));
        } else if (this.tempo % 100 / 10 == 1) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n1));
        } else if (this.tempo % 100 / 10 == 2) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n2));
        } else if (this.tempo % 100 / 10 == 3) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n3));
        } else if (this.tempo % 100 / 10 == 4) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n4));
        } else if (this.tempo % 100 / 10 == 5) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n5));
        } else if (this.tempo % 100 / 10 == 6) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n6));
        } else if (this.tempo % 100 / 10 == 7) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n7));
        } else if (this.tempo % 100 / 10 == 8) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n8));
        } else if (this.tempo % 100 / 10 == 9) {
            TempoDigits2.setVisible(true);
            TempoDigits2.setIcon(new ImageIcon(n9));
        }
        
        if (this.tempo % 100 % 10 == 0) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n0));
        } else if (this.tempo % 100 % 10 == 1) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n1));
        } else if (this.tempo % 100 % 10 == 2) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n2));
        } else if (this.tempo % 100 % 10 == 3) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n3));
        } else if (this.tempo % 100 % 10 == 4) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n4));
        } else if (this.tempo % 100 % 10 == 5) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n5));
        } else if (this.tempo % 100 % 10 == 6) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n6));
        } else if (this.tempo % 100 % 10 == 7) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n7));
        } else if (this.tempo % 100 % 10 == 8) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n8));
        } else if (this.tempo % 100 % 10 == 9) {
            TempoDigits1.setVisible(true);
            TempoDigits1.setIcon(new ImageIcon(n9));
        }
    }
    
    public void setTimer(double timer) {
        this.timer = timer;
    }
    
    public void setMusicPlay(boolean musicPlay) {
        this.musicPlay = musicPlay;
    }
    
    public void run() {
        Play();
    }
    
    public void Play() {
        timer = 0;
        lastTime = System.nanoTime();
        while (true) {
            if (musicPlay) {
            long now = System.nanoTime();
            long memLastTime = lastTime;
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1) {
            timer += ticks;
            ticks = ticksPerQuarterNotes * tempo / 60 * ((now - memLastTime) / ns / 60);
            System.out.println("Tempo is " + tempo);
            
            if (!isPlaying) {
                TimeArrow.setPos( (int)(timer / ticksPerQuarterNotes * BarPanel.w) );
                TimeArrow.movePos();
            } else {
                TimeArrow.setPos( (int)(timer / ticksPerQuarterNotes * BarPanel.w * 4) );
                TimeArrow.movePos();
            }
            for (int j = 0; j < VVstiTrack.size(); j++) {
                VVstiTrack.get(j).setTimer(timer);
            }
            delta--;

            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }
    }
    }
}