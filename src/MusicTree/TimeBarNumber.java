/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.JLabel;

/**
 * 마디의 숫자를 표시할 때 숫자 하나하나의 객체이다.
 * 
 * @author 12151430 이건영
 */
public class TimeBarNumber extends JLabel {
    public void setLabelFont(int i) {
        this.setForeground(new Color(138, 150, 161));
         try {
            InputStream fontStream = getClass().getResourceAsStream("fonts/Fruity microfont.ttf");
            Font microFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            
            if (i % 4 == 1) {
                microFont = microFont.deriveFont(Font.BOLD, 18);
            } else {
                microFont = microFont.deriveFont(Font.PLAIN, 14);
            }
            
            fontStream.close();
            
            this.setFont(microFont);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
