/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicTree;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * 상단의 시간을 표시하는 클래스이다.
 * 스크롤바를 10 milliseconds 간격으로 갱신시킨다.
 * 
 * @author 12151430 이건영
 */
public class Timer implements Runnable {
    private Image n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    private Image n24_0, n24_1, n24_2, n24_3, n24_4, n24_5, n24_6, n24_7, n24_8, n24_9;
    private Image Mn0, Mn1, Mn2, Mn3, Mn4, Mn5, Mn6, Mn7, Mn8, Mn9;
    private JLabel d6, d5, d4, d3, d2, d1;
    private JLabel statusLabel;
    private JLabel Rdigit4, Rdigit3, Rdigit2, Rdigit1;
    private JLabel MemMeter;
    private TimeArrow TimeArrow;
    private BarPanel[] BarPanel;
    private DrawPanel[] DrawPanel;
    private BarRowLine[] BarRowLine;
    
    private JScrollPane trackScrollPane, timeScrollPane, projectScrollPane;
    private int memUsage = 0;
    private boolean isPlaying = false;
    
    private long used;
    private double percent;
    private double wd;
    
    private int bar_idx = 0;
    private int prev_idx = -1;
    private int posX;
    private int scrollX = 0;
    private int scrollY = 0;
    private int mSeconds;
    private int seconds;
    private int minutes;
    private VstiPanel VstiPanel;
    private Thread tv;
    
    private double nano = 0;
    Timer() {
        mSeconds = 0;
        posX = 0;
    }
    
    public void setVstiPanel(VstiPanel VstiPanel, Thread tv) {
        this.VstiPanel = VstiPanel;
        this.tv = tv;
    }
    
    public void setUsage(JLabel Rd4, JLabel Rd3, JLabel Rd2, JLabel Rd1, JLabel MemMeter) {
        this.Rdigit4 = Rd4;
        this.Rdigit3 = Rd3;
        this.Rdigit2 = Rd2;
        this.Rdigit1 = Rd1;
        this.MemMeter = MemMeter;
        
        try {
            Mn0 = ImageIO.read(getClass().getResource("resources/CPUDigits9_0.png"));
            Mn1 = ImageIO.read(getClass().getResource("resources/CPUDigits9_1.png"));
            Mn2 = ImageIO.read(getClass().getResource("resources/CPUDigits9_2.png"));
            Mn3 = ImageIO.read(getClass().getResource("resources/CPUDigits9_3.png"));
            Mn4 = ImageIO.read(getClass().getResource("resources/CPUDigits9_4.png"));
            Mn5 = ImageIO.read(getClass().getResource("resources/CPUDigits9_5.png"));
            Mn6 = ImageIO.read(getClass().getResource("resources/CPUDigits9_6.png"));
            Mn7 = ImageIO.read(getClass().getResource("resources/CPUDigits9_7.png"));
            Mn8 = ImageIO.read(getClass().getResource("resources/CPUDigits9_8.png"));
            Mn9 = ImageIO.read(getClass().getResource("resources/CPUDigits9_9.png"));
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setTimeArrow(TimeArrow TimeArrow) {
        this.TimeArrow = TimeArrow;
    }
    
    public void setBarPanel(BarPanel[] BarPanel) {
        this.BarPanel = BarPanel;
    }
    
    public void setBarRowLine(BarRowLine[] BarRowLine) {
        this.BarRowLine = BarRowLine;
    }
    
    public void setDrawPanel(DrawPanel[] DrawPanel) {
        this.DrawPanel = DrawPanel;
    }
    
    public void setScrollPane(JScrollPane trackScrollPane, JScrollPane timeScrollPane, JScrollPane projectScrollPane) {
        this.trackScrollPane = trackScrollPane;
        this.timeScrollPane = timeScrollPane;
        this.projectScrollPane = projectScrollPane;
    }
    
    public void setTimerDigits(JLabel d6, JLabel d5, JLabel d4, JLabel d3, JLabel d2, JLabel d1) {
        this.d6 = d6;
        this.d5 = d5;
        this.d4 = d4;
        this.d3 = d3;
        this.d2 = d2;
        this.d1 = d1;
        
        try {
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
            n24_0 = ImageIO.read(getClass().getResource("resources/TimeDigits24_0.png"));
            n24_1 = ImageIO.read(getClass().getResource("resources/TimeDigits24_1.png"));
            n24_2 = ImageIO.read(getClass().getResource("resources/TimeDigits24_2.png"));
            n24_3 = ImageIO.read(getClass().getResource("resources/TimeDigits24_3.png"));
            n24_4 = ImageIO.read(getClass().getResource("resources/TimeDigits24_4.png"));
            n24_5 = ImageIO.read(getClass().getResource("resources/TimeDigits24_5.png"));
            n24_6 = ImageIO.read(getClass().getResource("resources/TimeDigits24_6.png"));
            n24_7 = ImageIO.read(getClass().getResource("resources/TimeDigits24_7.png"));
            n24_8 = ImageIO.read(getClass().getResource("resources/TimeDigits24_8.png"));
            n24_9 = ImageIO.read(getClass().getResource("resources/TimeDigits24_9.png"));
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }
    
    public void run() {
        while (true) {
            update();
        }
    }
    
    public void update() { 
        try {
            Thread.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MemMeter.setSize(memUsage, 12);

        timeScrollPane.repaint();
        trackScrollPane.repaint();
        
        if(projectScrollPane.getHorizontalScrollBar().getValueIsAdjusting() || projectScrollPane.getVerticalScrollBar().getValueIsAdjusting()) {
            scrollX = projectScrollPane.getHorizontalScrollBar().getValue();
            scrollY = projectScrollPane.getVerticalScrollBar().getValue();
            
            timeScrollPane.getHorizontalScrollBar().setValue(scrollX);
            trackScrollPane.getVerticalScrollBar().setValue(scrollY);
            timeScrollPane.repaint();
            timeScrollPane.revalidate();
            trackScrollPane.repaint();
            trackScrollPane.revalidate();
        }
        
        if (isPlaying) {
            mSeconds++;
            bar_idx = TimeArrow.getPos() / 32;
            
            if (prev_idx != bar_idx) {
                prev_idx = bar_idx;
                BarPanel[bar_idx].setPlayingBackground();
                
                for (int i = 0; i < BarRowLine.length; i++) {
                    BarRowLine[i].repaint();
                }
                
                if (bar_idx > 0) {
                    BarPanel[bar_idx - 1].setNormalBackground();

                    if ((bar_idx - 1) % 4 == 0) {
                        BarPanel[bar_idx - 1].setBoldBorder();
                    } else {
                        BarPanel[bar_idx - 1].setNormalBorder();
                    }

                    if ((bar_idx - 1) % 32 < 16) {
                        BarPanel[bar_idx - 1].setNormalBackground();
                    } else {
                        BarPanel[bar_idx - 1].setBoldBackground();
                    }
                }
            }
        }
        
    /*       if (isPlaying) {
                TimeArrow.movePos();
                nano += 0.62;
                int tmp = (int)nano;
                TimeArrow.setPos(tmp);
            }
*/
            
            if(mSeconds >= 100) {
                mSeconds = 0;
                seconds++;
            }
            
            if(seconds >= 60) {
                seconds = 0;
                minutes++;
            }
            
            if(mSeconds / 10 == 0) {
                d2.setIcon(new ImageIcon(n0));
            } else if(mSeconds / 10 == 1) {
                d2.setIcon(new ImageIcon(n1));
            } else if(mSeconds / 10 == 2) {
                d2.setIcon(new ImageIcon(n2));
            } else if(mSeconds / 10 == 3) {
                d2.setIcon(new ImageIcon(n3));
            } else if(mSeconds / 10 == 4) {
                d2.setIcon(new ImageIcon(n4));
            } else if(mSeconds / 10 == 5) {
                d2.setIcon(new ImageIcon(n5));
            } else if(mSeconds / 10 == 6) {
                d2.setIcon(new ImageIcon(n6));
            } else if(mSeconds / 10 == 7) {
                d2.setIcon(new ImageIcon(n7));
            } else if(mSeconds / 10 == 8) {
                d2.setIcon(new ImageIcon(n8));
            } else if(mSeconds / 10 == 9) {
                d2.setIcon(new ImageIcon(n9));
            }
            
            if(mSeconds % 10 == 0) {
                d1.setIcon(new ImageIcon(n0));
            } else if(mSeconds % 10 == 1) {
                d1.setIcon(new ImageIcon(n1));
            } else if(mSeconds % 10 == 2) {
                d1.setIcon(new ImageIcon(n2));
            } else if(mSeconds % 10 == 3) {
                d1.setIcon(new ImageIcon(n3));
            } else if(mSeconds % 10 == 4) {
                d1.setIcon(new ImageIcon(n4));
            } else if(mSeconds % 10 == 5) {
                d1.setIcon(new ImageIcon(n5));
            } else if(mSeconds % 10 == 6) {
                d1.setIcon(new ImageIcon(n6));
            } else if(mSeconds % 10 == 7) {
                d1.setIcon(new ImageIcon(n7));
            } else if(mSeconds % 10 == 8) {
                d1.setIcon(new ImageIcon(n8));
            } else if(mSeconds % 10 == 9) {
                d1.setIcon(new ImageIcon(n9));
            }
            
            if(seconds / 10 == 0) {
                d4.setIcon(new ImageIcon(n24_0));
            } else if(seconds / 10 == 1) {
                d4.setIcon(new ImageIcon(n24_1));
            } else if(seconds / 10 == 2) {
                d4.setIcon(new ImageIcon(n24_2));
            } else if(seconds / 10 == 3) {
                d4.setIcon(new ImageIcon(n24_3));
            } else if(seconds / 10 == 4) {
                d4.setIcon(new ImageIcon(n24_4));
            } else if(seconds / 10 == 5) {
                d4.setIcon(new ImageIcon(n24_5));
            } else if(seconds / 10 == 6) {
                d4.setIcon(new ImageIcon(n24_6));
            } else if(seconds / 10 == 7) {
                d4.setIcon(new ImageIcon(n24_7));
            } else if(seconds / 10 == 8) {
                d4.setIcon(new ImageIcon(n24_8));
            } else if(seconds / 10 == 9) {
                d4.setIcon(new ImageIcon(n24_9));
            }
            
            if(seconds % 10 == 0) {
                d3.setIcon(new ImageIcon(n24_0));
            } else if(seconds % 10 == 1) {
                d3.setIcon(new ImageIcon(n24_1));
            } else if(seconds % 10 == 2) {
                d3.setIcon(new ImageIcon(n24_2));
            } else if(seconds % 10 == 3) {
                d3.setIcon(new ImageIcon(n24_3));
            } else if(seconds % 10 == 4) {
                d3.setIcon(new ImageIcon(n24_4));
            } else if(seconds % 10 == 5) {
                d3.setIcon(new ImageIcon(n24_5));
            } else if(seconds % 10 == 6) {
                d3.setIcon(new ImageIcon(n24_6));
            } else if(seconds % 10 == 7) {
                d3.setIcon(new ImageIcon(n24_7));
            } else if(seconds % 10 == 8) {
                d3.setIcon(new ImageIcon(n24_8));
            } else if(seconds % 10 == 9) {
                d3.setIcon(new ImageIcon(n24_9));
            }
            
            if(minutes / 10 == 0) {
                d6.setIcon(new ImageIcon(n24_0));
            } else if(minutes / 10 == 1) {
                d6.setIcon(new ImageIcon(n24_1));
            } else if(minutes / 10 == 2) {
                d6.setIcon(new ImageIcon(n24_2));
            } else if(minutes / 10 == 3) {
                d6.setIcon(new ImageIcon(n24_3));
            } else if(minutes / 10 == 4) {
                d6.setIcon(new ImageIcon(n24_4));
            } else if(minutes / 10 == 5) {
                d6.setIcon(new ImageIcon(n24_5));
            } else if(minutes / 10 == 6) {
                d6.setIcon(new ImageIcon(n24_6));
            } else if(minutes / 10 == 7) {
                d6.setIcon(new ImageIcon(n24_7));
            } else if(minutes / 10 == 8) {
                d6.setIcon(new ImageIcon(n24_8));
            } else if(minutes / 10 == 9) {
                d6.setIcon(new ImageIcon(n24_9));
            }
            
            if(minutes % 10 == 0) {
                d5.setIcon(new ImageIcon(n24_0));
            } else if(minutes % 10 == 1) {
                d5.setIcon(new ImageIcon(n24_1));
            } else if(minutes % 10 == 2) {
                d5.setIcon(new ImageIcon(n24_2));
            } else if(minutes % 10 == 3) {
                d5.setIcon(new ImageIcon(n24_3));
            } else if(minutes % 10 == 4) {
                d5.setIcon(new ImageIcon(n24_4));
            } else if(minutes % 10 == 5) {
                d5.setIcon(new ImageIcon(n24_5));
            } else if(minutes % 10 == 6) {
                d5.setIcon(new ImageIcon(n24_6));
            } else if(minutes % 10 == 7) {
                d5.setIcon(new ImageIcon(n24_7));
            } else if(minutes % 10 == 8) {
                d5.setIcon(new ImageIcon(n24_8));
            } else if(minutes % 10 == 9) {
                d5.setIcon(new ImageIcon(n24_9));
            }
            
             used = (Runtime.getRuntime().totalMemory() / 1024 / 1024) - (Runtime.getRuntime().freeMemory() / 1024 / 1024);
             percent = (double)used / 1024 * 100;
             wd = 30 * (int) percent * 0.01;
             memUsage = (int) Math.ceil(wd);
        
        if (used / 1000 == 1) {
            Rdigit4.setIcon(new ImageIcon(Mn1));
        } else if (used / 1000 == 2) {
            Rdigit4.setIcon(new ImageIcon(Mn2));
        } else if (used / 1000 == 3) {
            Rdigit4.setIcon(new ImageIcon(Mn3));
        } else if (used / 1000 == 4) {
            Rdigit4.setIcon(new ImageIcon(Mn4));
        }
        
        if ((used % 1000) / 100 == 0) {
            Rdigit3.setIcon(new ImageIcon(Mn0));
        } else if ((used % 1000) / 100 == 1) {
            Rdigit3.setIcon(new ImageIcon(Mn1));
        } else if ((used % 1000) / 100 == 2) {
            Rdigit3.setIcon(new ImageIcon(Mn2));
        } else if ((used % 1000) / 100 == 3) {
            Rdigit3.setIcon(new ImageIcon(Mn3));
        } else if ((used % 1000) / 100 == 4) {
            Rdigit3.setIcon(new ImageIcon(Mn4));
        } else if ((used % 1000) / 100 == 5) {
            Rdigit3.setIcon(new ImageIcon(Mn5));
        } else if ((used % 1000) / 100 == 6) {
            Rdigit3.setIcon(new ImageIcon(Mn6));
        } else if ((used % 1000) / 100 == 7) {
            Rdigit3.setIcon(new ImageIcon(Mn7));
        } else if ((used % 1000) / 100 == 8) {
            Rdigit3.setIcon(new ImageIcon(Mn8));
        } else if ((used % 1000) / 100 == 9) {
            Rdigit3.setIcon(new ImageIcon(Mn9));
        }
        
        if ((used % 100) / 10 == 0) {
            Rdigit2.setIcon(new ImageIcon(Mn0));
        } else if ((used % 100) / 10 == 1) {
            Rdigit2.setIcon(new ImageIcon(Mn1));
        } else if ((used % 100) / 10 == 2) {
            Rdigit2.setIcon(new ImageIcon(Mn2));
        } else if ((used % 100) / 10 == 3) {
            Rdigit2.setIcon(new ImageIcon(Mn3));
        } else if ((used % 100) / 10 == 4) {
            Rdigit2.setIcon(new ImageIcon(Mn4));
        } else if ((used % 100) / 10 == 5) {
            Rdigit2.setIcon(new ImageIcon(Mn5));
        } else if ((used % 100) / 10 == 6) {
            Rdigit2.setIcon(new ImageIcon(Mn6));
        } else if ((used % 100) / 10 == 7) {
            Rdigit2.setIcon(new ImageIcon(Mn7));
        } else if ((used % 100) / 10 == 8) {
            Rdigit2.setIcon(new ImageIcon(Mn8));
        } else if ((used % 100) / 10 == 9) {
            Rdigit2.setIcon(new ImageIcon(Mn9));
        }
        
       if ((used % 10) == 0) {
            Rdigit1.setIcon(new ImageIcon(Mn0));
        } else if ((used % 10) == 1) {
            Rdigit1.setIcon(new ImageIcon(Mn1));
        } else if ((used % 10) == 2) {
            Rdigit1.setIcon(new ImageIcon(Mn2));
        } else if ((used % 10) == 3) {
            Rdigit1.setIcon(new ImageIcon(Mn3));
        } else if ((used % 10) == 4) {
            Rdigit1.setIcon(new ImageIcon(Mn4));
        } else if ((used % 10) == 5) {
            Rdigit1.setIcon(new ImageIcon(Mn5));
        } else if ((used % 10) == 6) {
            Rdigit1.setIcon(new ImageIcon(Mn6));
        } else if ((used % 10) == 7) {
            Rdigit1.setIcon(new ImageIcon(Mn7));
        } else if ((used % 10) == 8) {
            Rdigit1.setIcon(new ImageIcon(Mn8));
        } else if ((used % 10) == 9) {
            Rdigit1.setIcon(new ImageIcon(Mn9));
        }   
    }
    
    public void pause() {
        isPlaying = false;
        int m1, m2, s1, s2, ms1, ms2;
       
        m1 = minutes / 10;
        m2 = minutes % 10;
        s1 = seconds / 10;
        s2 = seconds % 10;
        ms1 = mSeconds / 10;
        ms2 = mSeconds % 10;
        statusLabel.setText("Paused at " + m1 + m2 + ":" + s1 + s2 + ":" + ms1 + ms2);
    }
    
    public void play() {
        isPlaying = true;
        statusLabel.setText("Now Playing...");
    }
    
    public void reset() {
        if (bar_idx > 0) {
                    BarPanel[bar_idx].setNormalBackground();

                    if ((bar_idx) % 4 == 0) {
                        BarPanel[bar_idx].setBoldBorder();
                    } else {
                        BarPanel[bar_idx].setNormalBorder();
                    }

                    if ((bar_idx) % 32 < 16) {
                        BarPanel[bar_idx].setNormalBackground();
                    } else {
                        BarPanel[bar_idx].setBoldBackground();
                    }
                }
        
        isPlaying = false;
        nano = 0;
        posX = 0;
        TimeArrow.resetPos();
        mSeconds = 0;
        seconds = 0;
        minutes = 0;
        statusLabel.setText("Paused at 00:00:00");
    }
    
    public boolean getIsPlaying() {
        return isPlaying;
    }
}
