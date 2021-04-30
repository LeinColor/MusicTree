/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * 스크롤바를 회색으로 바꾸는 UI클래스이다.
 * 
 * @author 12151430 이건영
 */


public class ScrollBarUI extends BasicScrollBarUI {
	Color one, two;
        boolean isPianoRoll = false;
	public ScrollBarUI(){
            one = new Color(61, 70, 75);
            two = new Color(95, 104, 109);
	}
        
        public ScrollBarUI(boolean isPianoRoll) {
            this.isPianoRoll = isPianoRoll;
            one = new Color(61, 70, 75);
            two = new Color(95, 104, 109);
        }
 
	@Override
	protected JButton createDecreaseButton(int orientation) {
            JButton button = new JButton();
	    Dimension zeroDim = new Dimension(0, 0);
	    button.setPreferredSize(zeroDim);
	    button.setMinimumSize(zeroDim);
	    button.setMaximumSize(zeroDim);
            button.setBackground(new Color(95, 104, 109));
	    return button;
	}
 
	@Override
	protected JButton createIncreaseButton(int orientation) {
            JButton button = new JButton();
	    Dimension zeroDim = new Dimension(0, 0);
	    button.setPreferredSize(zeroDim);
	    button.setMinimumSize(zeroDim);
	    button.setMaximumSize(zeroDim);
            button.setBackground(new Color(95, 104, 109));
	    return button;
	}
 
	@Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        if (!isPianoRoll) {
            g.setColor(one);
            g.translate(trackBounds.x, trackBounds.y);
            g.fillRect(0, 0, 2000, 2000);
            g.translate(-trackBounds.x, -trackBounds.y);
        } else {
            g.setColor(one);
            g.translate(trackBounds.x, trackBounds.y);
            g.fillRect(0, 0, 2000, 2000);
            g.translate(-trackBounds.x, -trackBounds.y);
        }
    }
 
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (!isPianoRoll) {
            g.setColor(two);
            g.translate(thumbBounds.x, thumbBounds.y);
            g.fillRect(0, 0, 194, 501); // 477, 501
            g.translate(-thumbBounds.x, -thumbBounds.y);
        } else {
            g.setColor(two);
            g.translate(thumbBounds.x, thumbBounds.y);
            g.fillRect(0, 0, 100, 281); // 477, 501
            g.translate(-thumbBounds.x, -thumbBounds.y);
    }
}
}