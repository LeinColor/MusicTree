/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;

/**
 * 미사용 클래스
 * 
 * @author 12151430 이건영
 */
public class UpdatePane implements Runnable {
    private JScrollPane trackScrollPane, timeScrollPane, projectScrollPane;
    
    public void run() {
        while(true) {
            try {
                update();
            } catch (InterruptedException ex) {
                Logger.getLogger(UpdatePane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setScrollPane(JScrollPane trackScrollPane, JScrollPane timeScrollPane, JScrollPane projectScrollPane) {
        this.trackScrollPane = trackScrollPane;
        this.timeScrollPane = timeScrollPane;
        this.projectScrollPane = projectScrollPane;
    }
    
    public void update() throws InterruptedException {
        Thread.sleep(50);
        
        timeScrollPane.repaint();
        timeScrollPane.revalidate();
        trackScrollPane.repaint();
        trackScrollPane.revalidate();
    }
}
