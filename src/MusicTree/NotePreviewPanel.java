/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * 분홍색 미니노트를 미리 보여주는 패널이다.
 * 
 * @author 12151430 이건영
 */
public class NotePreviewPanel extends JPanel {
    public NotePreviewPanel(ArrayList<MidiEvent> event, int ticks) {
        NotePreviewed Note = new NotePreviewed();
        int starttime = 0;
        int endtime = 0;
        int notenumber = 0;
        int x1 = 0;
        int x2 = 0;
        
        this.setBackground(new Color(255, 255, 255));
        
        for (int i = 0; i < event.size(); i++) {
            if (event.get(i).eventType == MidiFile.EventNoteOn && event.get(i).velocity > 0) {
                starttime = event.get(i).startTime;
                x1 = starttime / ticks * 15;
                endtime = starttime + event.get(i).deltaTime;
                x2 = endtime / ticks * 15;
                notenumber= event.get(i).noteNumber;
                
                Note.setW(x2 - x1);
                Note.setLocation(x1, 40 - 20);
                
                this.add(Note);
            }
        }
    }
}