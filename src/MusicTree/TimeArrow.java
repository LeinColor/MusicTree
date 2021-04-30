/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 재생 지점을 가리키는 파란색 화살표이다.
 * 
 * @author 12151430 이건영
 */
public class TimeArrow extends JLabel {
    private Image arrowIcon;
    
    private int pos = -7;
    private boolean isPlaying = false;
    
    TimeArrow() {
        this.setLocation(-7 ,0);
        this.setSize(15, 12);
    }
    
    public void setIcon() throws IOException {
        arrowIcon = ImageIO.read(getClass().getResource("resources/timeArrow.png"));
        this.setIcon(new ImageIcon(arrowIcon));
    }
    
    public void movePos() {
        this.setLocation(pos, 0);
    }
    
    public void resetPos() {
        this.setLocation(-7, 0);
        pos = -7;
    }
    
    public void setPos(int pos) {
        this.pos = pos;
    }
    
    public int getPos() {
        return pos;
    }
}
