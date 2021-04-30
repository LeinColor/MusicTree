/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package MusicTree;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Graphic extends javax.swing.JFrame implements KeyListener, MouseListener {
    
    private MidiFile openFile;
    private Timer timer;
    private TimeArrow TimeArrow;
    private UpdateOffset UpdateOffset;
    private Thread tm;
    private Thread a, b;
    private Image img;
    private Image checkFalse01, checkTrue01;
    private Image imgStepBeat01, imgStepBeat02;
    private Image imgBarMinutes01, imgBarMinutes02;
    private Image imgViewPlay01, imgViewPlay02;
    private Image imgViewStep01, imgViewStep02;
    private Image imgViewPiano01, imgViewPiano02;
    private Image imgViewBrowser01, imgViewBrowser02;
    private Image imgViewMixer01, imgViewMixer02;
    private Image imgUndo01, imgUndo02;
    private Image imgSave01, imgSave02;
    private Image imgRender01, imgRender02;
    private Image imgAudioEditor01, imgAudioEditor02;
    private Image imgRecording01, imgRecording02;
    private Image imgInfo01, imgInfo02;
    private Image imgAbout01, imgAbout02;
    private Image imgMin01, imgMin02;
    private Image imgMax01, imgMax02;
    private Image imgClose01, imgClose02;
    private Image imgSyncA01, imgSyncA02;
    private Image imgSyncB01, imgSyncB02;
    private Image imgFile01, imgFile02;
    private Image imgEdit01, imgEdit02;
    private Image imgChannels01, imgChannels02;
    private Image imgView01, imgView02;
    private Image imgOptions01, imgOptions02;
    private Image imgTools01, imgTools02;
    private Image imgHelp01, imgHelp02;
    private Image imgEEPlay;
    private Image imgDraw01, imgDraw02;
    private Image imgPaint01, imgPaint02;
    private Image imgDelete01, imgDelete02;
    private Image imgMute01, imgMute02;
    private Image imgSlip01, imgSlip02;
    private Image imgSlice01, imgSlice02;
    private Image imgSelect01, imgSelect02;
    private Image imgZoom01, imgZoom02;
    private Image imgPlayback01, imgPlayback02;
    
    public static enum TOOL { DRAW, PAINT, DELETE, MUTE, SLIP, SLICE, SELECT, ZOOM, PLAYBACK }
    public static TOOL TOOL;
    
    private TrackPanel[] TrackPanel;
    private DrawPanel[] DrawPanel;
    private BarPanel[] BarPanel;
    private BarRowLine[] barRowLine;
    private PianoRoll pianoRoll;
    private PianoRollScreen pianoRollScreen;
    
    private ArrayList<TrackPanel> VTrackPanel = new ArrayList<TrackPanel>();
    private ArrayList<DrawPanel> VDrawPanel = new ArrayList<DrawPanel>();
    private ArrayList<VstiTrack> VVstiTrack = new ArrayList<VstiTrack>();
    private ArrayList<ArrayList<MidiEvent>> Track = new ArrayList<ArrayList<MidiEvent>>();
    private ArrayList<ArrayList<PianoNote>> Notes = new ArrayList<ArrayList<PianoNote>>();
    private int VTrackPanelCount = 0;
    private int trackCounts = 0;
    private int tempo = 120;
    private int ticks = 256;
    
    private ArrayList<ArrayList<MidiEvent>> Tracks;
    
    private TimeBarNumber[] TimeBarNumber;
    private VstiPanel VstiPanel;
    private boolean[] recordToolBarCheckBox = new boolean[10];
    private boolean switchStepBeat = false;
    private boolean switchBarMinutes = true;
    private boolean menuClicked = false;
    private boolean projectStart = false;
    private boolean drawMousePressed = false;
    private boolean isTempoPressed = false;
    private boolean isPianoRoll = false;
    private int menuToolbar = 0;
    private int memX = 0;
    private int memY = 0;
    private int memIdx = -1;
    private int memTempoY = 0;
    private int selectIdx = 0;
    
    /**
     * Creates new form ContactEditor
     */
    public Graphic() {
        initComponents();
        //menuBar.setVisible(false);
        this.setExtendedState(this.MAXIMIZED_BOTH);
        setIcon();
        addKeyListener(this);
        pianoRoll = new PianoRoll();
        pianoRollScreen = new PianoRollScreen();
        jPanel1.setVisible(false);
        for (int i = 0; i < 4; i++) {
            recordToolBarCheckBox[i] = true;
        }
    }
    
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        
        if (projectStart) {
        switch(keycode) {
            case KeyEvent.VK_SPACE:
                if (timer.getIsPlaying() == false) {
                    try {
                        img = ImageIO.read(getClass().getResource("resources/TransportPlay2.png"));
                        imgEEPlay = ImageIO.read(getClass().getResource("resources/EEPlay2.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    btn_EEPlay.setIcon(new ImageIcon(imgEEPlay));
                    playButton.setIcon(new ImageIcon(img));
                    try {
                        img = ImageIO.read(getClass().getResource("resources/TransportStop.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    stopButton.setIcon(new ImageIcon(img));
                    timer.play();
                    
                    UpdateOffset.setTimeArrow(TimeArrow);
                    UpdateOffset.setProperty(tempo, ticks);
                    UpdateOffset.setVVstiTrack(VVstiTrack);
                    UpdateOffset.setMusicPlay(true);
                    b = new Thread(UpdateOffset);
                    
                    for (int j = 0; j < VVstiTrack.size(); j++) {
                        VVstiTrack.get(j).setVstiTrack(Track.get(j));
                        VVstiTrack.get(j).setTempo(tempo);
                        VVstiTrack.get(j).setUpdateOffset(UpdateOffset);
                        VVstiTrack.get(j).setTicksPerQuarterNotes(ticks);
                        Thread a = new Thread(VVstiTrack.get(j));
                        a.start();
                    }
                    
                    b.start();
                    
                    
                    
                    if (VstiPanel != null) {
                        Thread tv = new Thread(VstiPanel);
                        VstiPanel.setTracks(Tracks);
                        timer.setVstiPanel(VstiPanel, tv);
                    }
                } else {
                    try {
                        img = ImageIO.read(getClass().getResource("resources/TransportPlay.png"));
                        imgEEPlay = ImageIO.read(getClass().getResource("resources/EEPlay.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    btn_EEPlay.setIcon(new ImageIcon(imgEEPlay));
                    playButton.setIcon(new ImageIcon(img));
                    try {
                        img = ImageIO.read(getClass().getResource("resources/TransportStop2.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    stopButton.setIcon(new ImageIcon(img));
                    UpdateOffset.setMusicPlay(false);
                    timer.pause();
                }
                break;
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
    }
    
    public void keyTyped(KeyEvent e) {
        int keycode = e.getKeyCode();
    }
    
     public void mousePressed(MouseEvent e) {
         if (e.getButton() == MouseEvent.BUTTON1 && e.getSource() != btn_viewPiano) {
             selectIdx = -1;
            for (int i = 0; i < VTrackPanel.size(); i++) {
                if (e.getSource() == VTrackPanel.get(i)) {
                   selectIdx = i;
                   channelLabel.setText("Channel " + (VTrackPanel.get(i).getChannel() + 1));
                   if (VTrackPanel.get(i).getChannel() == 9)
                       VTrackPanel.get(i).setInstrument(128);
                   instLabel.setText(MidiFile.Instruments[VTrackPanel.get(i).getInstrument()]);
                   trackNameLabel.setText("Staff-" + VTrackPanel.get(i).getTrackNumber());
               }
           }

          for (int i = 0; i < VTrackPanel.size(); i++) {
              if (VTrackPanel.get(i).getIsClicked()) {
                   VTrackPanel.get(i).setIsClicked(false);
                   VTrackPanel.get(i).repaint();
                   VTrackPanel.get(i).repaintMute();
                   VTrackPanel.get(i).repaintMixer();
              }
          }
          
          if (selectIdx >= 0) {
              System.out.println("Now selectIdx : " + selectIdx);
                VTrackPanel.get(selectIdx).setIsClicked(true);
                VTrackPanel.get(selectIdx).repaint();
          }
          
        if (e.getSource() == projectPanel && !drawMousePressed && TOOL == TOOL.DRAW) {
            DrawPanel drawing = new DrawPanel();
            VDrawPanel.add(drawing);
            drawing.setBorder();
            drawing.setColor(new Color(255, 255, 255, 120));
            drawing.setBackgroundColor();
            drawing.addMouseListener(this);
            drawMousePressed = true;
            memX = 32 * (e.getX() / 32) + 2;
            memY = (TrackPanel[0].HEIGHT + 1) * (e.getY() / TrackPanel[0].HEIGHT);
            memIdx++;
            
            drawing.setLocation(memX, memY);
            projectPanel.add(drawing, 1, -1);
        }
        
        if (TOOL == TOOL.SELECT && e.getSource() != btn_viewPiano) {
            selectIdx = -1;
            for (int i = 0; i < VDrawPanel.size(); i++) {
                if (e.getSource() == VDrawPanel.get(i)) {
                    selectIdx = i;
                    System.out.println(i);
                }
                  if (VDrawPanel.get(i).getIsClicked()) {
                    VDrawPanel.get(i).setIsClicked(false, VTrackPanel.get(i).getColor());
                    VDrawPanel.get(i).repaint();
                    projectPanel.repaint();
                  }
              }
            
            if(VDrawPanel.size() > 0 && selectIdx >= 0) {
                VDrawPanel.get(selectIdx).setIsClicked(true);
                VDrawPanel.get(selectIdx).repaint();
                projectPanel.repaint();
                }
            }
        }
         
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (e.getSource() == InstrumentBar) {
                PopupTrack.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
     
    public void mouseReleased(MouseEvent e) {
        if(drawMousePressed) {
            drawMousePressed = false;
            VDrawPanel.get(memIdx).setColor(VTrackPanel.get(memY / 41).getColor());
            VDrawPanel.get(memIdx).setBackgroundColor();
            projectPanel.repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopupFile = new javax.swing.JPopupMenu();
        newItem = new javax.swing.JMenuItem();
        openItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        saveItem = new javax.swing.JMenuItem();
        saveAsItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        ImportMenu = new javax.swing.JMenu();
        importMIDIItem = new javax.swing.JMenuItem();
        importAudioItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        exportWaveItem = new javax.swing.JMenuItem();
        exportMP3Item = new javax.swing.JMenuItem();
        exportMIDIItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        exitItem = new javax.swing.JMenuItem();
        PopupTrack = new javax.swing.JPopupMenu();
        addInstItem = new javax.swing.JMenuItem();
        addAudioItem = new javax.swing.JMenuItem();
        addSampleOne = new javax.swing.JMenuItem();
        border_top = new javax.swing.JLabel();
        border_left = new javax.swing.JLabel();
        statusBar = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        TimeDigits6 = new javax.swing.JLabel();
        TimeDigits5 = new javax.swing.JLabel();
        TimeDigits4 = new javax.swing.JLabel();
        TimeDigits3 = new javax.swing.JLabel();
        TimeDigits2 = new javax.swing.JLabel();
        TimeDigits1 = new javax.swing.JLabel();
        btn_switchStepBeat = new javax.swing.JButton();
        btn_switchBarMinutes = new javax.swing.JButton();
        TimeToolBar = new javax.swing.JLabel();
        TempoDigits3 = new javax.swing.JLabel();
        TempoDigits2 = new javax.swing.JLabel();
        TempoDigits1 = new javax.swing.JLabel();
        PatternDigits1 = new javax.swing.JLabel();
        btn_ps = new javax.swing.JButton();
        TransportToolBar = new javax.swing.JLabel();
        CheckBox1 = new javax.swing.JButton();
        CheckBox2 = new javax.swing.JButton();
        CheckBox3 = new javax.swing.JButton();
        CheckBox4 = new javax.swing.JButton();
        CheckBox5 = new javax.swing.JButton();
        CheckBox6 = new javax.swing.JButton();
        CheckBox7 = new javax.swing.JButton();
        CheckBox8 = new javax.swing.JButton();
        CheckBox9 = new javax.swing.JButton();
        CheckBox10 = new javax.swing.JButton();
        RecordToolBar = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        fileNameLabel = new javax.swing.JLabel();
        btn_min = new javax.swing.JButton();
        btn_max = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        btn_syncA = new javax.swing.JButton();
        btn_syncB = new javax.swing.JButton();
        btn_file = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_channels = new javax.swing.JButton();
        btn_view = new javax.swing.JButton();
        btn_options = new javax.swing.JButton();
        btn_tools = new javax.swing.JButton();
        btn_help = new javax.swing.JButton();
        ControlToolBar = new javax.swing.JLabel();
        MonitorToolBar = new javax.swing.JLabel();
        MemMeterFill = new javax.swing.JLabel();
        MemMeter = new javax.swing.JLabel();
        RAMDigit4 = new javax.swing.JLabel();
        RAMDigit3 = new javax.swing.JLabel();
        RAMDigit2 = new javax.swing.JLabel();
        RAMDigit1 = new javax.swing.JLabel();
        CPUPolyDigit4 = new javax.swing.JLabel();
        CPUPolyDigit3 = new javax.swing.JLabel();
        CPUPolyDigit2 = new javax.swing.JLabel();
        CPUPolyDigit1 = new javax.swing.JLabel();
        CPUDigit2 = new javax.swing.JLabel();
        CPUDigit1 = new javax.swing.JLabel();
        CPUMeter = new javax.swing.JLabel();
        CPUToolBar = new javax.swing.JLabel();
        btn_viewPlay = new javax.swing.JButton();
        btn_viewStep = new javax.swing.JButton();
        btn_viewPiano = new javax.swing.JButton();
        btn_viewBrowser = new javax.swing.JButton();
        btn_viewMixer = new javax.swing.JButton();
        ShortcutToolBar = new javax.swing.JLabel();
        btn_undo = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_render = new javax.swing.JButton();
        btn_audioEditor = new javax.swing.JButton();
        btn_recording = new javax.swing.JButton();
        btn_info = new javax.swing.JButton();
        btn_about = new javax.swing.JButton();
        ShortcutToolBar2 = new javax.swing.JLabel();
        Copyright = new javax.swing.JLabel();
        OnlineToolBar = new javax.swing.JLabel();
        backgroundPanel = new javax.swing.JPanel();
        background = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        projectScrollPane = new javax.swing.JScrollPane();
        projectPanel = new javax.swing.JLayeredPane();
        trackInfoBar = new javax.swing.JPanel();
        trackRightBorder = new javax.swing.JPanel();
        trackLeftBorder = new javax.swing.JPanel();
        trackWindowPanel = new javax.swing.JPanel();
        btn_browserOptions = new javax.swing.JButton();
        btn_collapseStructure = new javax.swing.JButton();
        btn_refreshStructure = new javax.swing.JButton();
        trackWindow = new javax.swing.JLabel();
        btn_closeStructure = new javax.swing.JButton();
        trackNamePanel = new javax.swing.JPanel();
        trackNameLabel = new javax.swing.JLabel();
        trackMixerIcon = new javax.swing.JLabel();
        trackNumberLabel = new javax.swing.JLabel();
        trackNameMarker = new javax.swing.JPanel();
        trackRightArrow = new javax.swing.JLabel();
        volumeIconLabel = new javax.swing.JLabel();
        volumeFillLabel = new javax.swing.JLabel();
        volumeEmptyLabel = new javax.swing.JLabel();
        volumeValueLabel = new javax.swing.JLabel();
        panelBorder1 = new javax.swing.JLabel();
        panIconLabel = new javax.swing.JLabel();
        panFillLabel = new javax.swing.JLabel();
        panValueLabel = new javax.swing.JLabel();
        panEmptyLabel = new javax.swing.JLabel();
        panelBorder2 = new javax.swing.JLabel();
        midiInputIconLabel = new javax.swing.JLabel();
        midiInputLabel = new javax.swing.JLabel();
        panelBorder3 = new javax.swing.JLabel();
        instIconLabel = new javax.swing.JLabel();
        instLabel = new javax.swing.JLabel();
        panelBorder4 = new javax.swing.JLabel();
        channelIconLabel = new javax.swing.JLabel();
        channelLabel = new javax.swing.JLabel();
        panelBorder5 = new javax.swing.JLabel();
        pianoRollPanel = new javax.swing.JPanel();
        pianoRollLabel = new javax.swing.JLabel();
        pianoRollIcon = new javax.swing.JLabel();
        eventListPanel = new javax.swing.JPanel();
        eventListLabel = new javax.swing.JLabel();
        eventListIcon = new javax.swing.JLabel();
        insertsPanel = new javax.swing.JPanel();
        insertsLabel = new javax.swing.JLabel();
        insertsIcon = new javax.swing.JLabel();
        sendsPanel = new javax.swing.JPanel();
        sendsLabel = new javax.swing.JLabel();
        sendsIcon = new javax.swing.JLabel();
        keyboardPanel = new javax.swing.JPanel();
        keyboardLabel = new javax.swing.JLabel();
        keyboardIcon = new javax.swing.JLabel();
        trackScrollPane = new javax.swing.JScrollPane();
        InstrumentBar = new javax.swing.JPanel();
        jFileChooser1 = new javax.swing.JFileChooser();
        PLToolbarPanel = new javax.swing.JPanel();
        btn_playListOptions = new javax.swing.JButton();
        btn_snapToGrid = new javax.swing.JButton();
        btn_draw = new javax.swing.JButton();
        btn_paint = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_mute = new javax.swing.JButton();
        btn_slip = new javax.swing.JButton();
        btn_slice = new javax.swing.JButton();
        btn_select = new javax.swing.JButton();
        btn_zoom = new javax.swing.JButton();
        btn_playback = new javax.swing.JButton();
        btn_EEPlay = new javax.swing.JButton();
        playListLabel = new javax.swing.JLabel();
        timeScrollPane = new javax.swing.JScrollPane();
        timeBar = new javax.swing.JLayeredPane();
        timeNumLabel = new javax.swing.JLabel();

        newItem.setText("New");
        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemActionPerformed(evt);
            }
        });
        PopupFile.add(newItem);

        openItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openItem.setText("Open");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openItemActionPerformed(evt);
            }
        });
        PopupFile.add(openItem);
        PopupFile.add(jSeparator1);

        saveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveItem.setText("Save");
        PopupFile.add(saveItem);

        saveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsItem.setText("Save as");
        PopupFile.add(saveAsItem);
        PopupFile.add(jSeparator2);

        ImportMenu.setText("Import");

        importMIDIItem.setText("MIDI file...");
        ImportMenu.add(importMIDIItem);

        importAudioItem.setText("Audio file...");
        ImportMenu.add(importAudioItem);

        PopupFile.add(ImportMenu);

        exportMenu.setText("Export");

        exportWaveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        exportWaveItem.setText("Wave file...");
        exportMenu.add(exportWaveItem);

        exportMP3Item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exportMP3Item.setText("MP3 file...");
        exportMenu.add(exportMP3Item);

        exportMIDIItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exportMIDIItem.setText("MIDI file...");
        exportMenu.add(exportMIDIItem);

        PopupFile.add(exportMenu);
        PopupFile.add(jSeparator3);

        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        PopupFile.add(exitItem);

        addInstItem.setText("Add Instrument Track");
        addInstItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInstItemActionPerformed(evt);
            }
        });
        PopupTrack.add(addInstItem);

        addAudioItem.setText("Add Audio Item");
        PopupTrack.add(addAudioItem);

        addSampleOne.setText("Add 5ample One");
        PopupTrack.add(addSampleOne);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Music Tree");
        setBackground(new java.awt.Color(39, 40, 34));
        setUndecorated(true);

        border_top.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/border_top.png"))); // NOI18N

        border_left.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/border_left.png"))); // NOI18N

        statusBar.setBackground(new java.awt.Color(9, 9, 9));
        statusBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        statusBar.setPreferredSize(new java.awt.Dimension(1920, 95));
        statusBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resetButton.setFont(new java.awt.Font("굴림", 1, 18)); // NOI18N
        resetButton.setForeground(new java.awt.Color(0, 64, 128));
        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TransportReset.png"))); // NOI18N
        resetButton.setFocusable(false);
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButtonMouseExited(evt);
            }
        });
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        resetButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resetButtonKeyPressed(evt);
            }
        });
        statusBar.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 45, 22, 21));

        playButton.setFont(new java.awt.Font("굴림", 1, 18)); // NOI18N
        playButton.setForeground(new java.awt.Color(0, 150, 0));
        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TransportPlay.png"))); // NOI18N
        playButton.setFocusable(false);
        playButton.setPreferredSize(new java.awt.Dimension(55, 21));
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButtonMouseExited(evt);
            }
        });
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        statusBar.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 45, 22, -1));

        stopButton.setFont(new java.awt.Font("굴림", 1, 18)); // NOI18N
        stopButton.setForeground(new java.awt.Color(255, 0, 0));
        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TransportStop.png"))); // NOI18N
        stopButton.setFocusable(false);
        stopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stopButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                stopButtonMouseExited(evt);
            }
        });
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        statusBar.add(stopButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 45, 22, 21));

        TimeDigits6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits24_0.png"))); // NOI18N
        statusBar.add(TimeDigits6, new org.netbeans.lib.awtextra.AbsoluteConstraints(414, 8, -1, -1));

        TimeDigits5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits24_0.png"))); // NOI18N
        statusBar.add(TimeDigits5, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 8, -1, -1));

        TimeDigits4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits24_0.png"))); // NOI18N
        statusBar.add(TimeDigits4, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 8, -1, -1));

        TimeDigits3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits24_0.png"))); // NOI18N
        statusBar.add(TimeDigits3, new org.netbeans.lib.awtextra.AbsoluteConstraints(476, 8, -1, -1));

        TimeDigits2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_0.png"))); // NOI18N
        statusBar.add(TimeDigits2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 15, -1, -1));

        TimeDigits1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_0.png"))); // NOI18N
        statusBar.add(TimeDigits1, new org.netbeans.lib.awtextra.AbsoluteConstraints(512, 15, -1, -1));

        btn_switchStepBeat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeBtn01.png"))); // NOI18N
        btn_switchStepBeat.setBorderPainted(false);
        btn_switchStepBeat.setFocusPainted(false);
        btn_switchStepBeat.setFocusable(false);
        btn_switchStepBeat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_switchStepBeatMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_switchStepBeatMouseExited(evt);
            }
        });
        btn_switchStepBeat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_switchStepBeatActionPerformed(evt);
            }
        });
        statusBar.add(btn_switchStepBeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 10, 10, 6));

        btn_switchBarMinutes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeBtn04.png"))); // NOI18N
        btn_switchBarMinutes.setBorderPainted(false);
        btn_switchBarMinutes.setFocusPainted(false);
        btn_switchBarMinutes.setFocusable(false);
        btn_switchBarMinutes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_switchBarMinutesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_switchBarMinutesMouseExited(evt);
            }
        });
        btn_switchBarMinutes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_switchBarMinutesActionPerformed(evt);
            }
        });
        statusBar.add(btn_switchBarMinutes, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 20, 10, 6));

        TimeToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeToolBar.png"))); // NOI18N
        statusBar.add(TimeToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, -1, -1));

        TempoDigits3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_1.png"))); // NOI18N
        statusBar.add(TempoDigits3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 47, -1, -1));

        TempoDigits2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_2.png"))); // NOI18N
        statusBar.add(TempoDigits2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 47, -1, -1));

        TempoDigits1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_0.png"))); // NOI18N
        statusBar.add(TempoDigits1, new org.netbeans.lib.awtextra.AbsoluteConstraints(521, 47, -1, -1));

        PatternDigits1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TimeDigits16_1.png"))); // NOI18N
        statusBar.add(PatternDigits1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 47, -1, -1));

        btn_ps.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_ps01.png"))); // NOI18N
        btn_ps.setBorderPainted(false);
        btn_ps.setFocusPainted(false);
        btn_ps.setFocusable(false);
        btn_ps.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_psMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_psMouseExited(evt);
            }
        });
        statusBar.add(btn_ps, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 45, 28, 21));

        TransportToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/TransportToolBar.png"))); // NOI18N
        TransportToolBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                TransportToolBarMouseDragged(evt);
            }
        });
        TransportToolBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TransportToolBarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                TransportToolBarMouseReleased(evt);
            }
        });
        statusBar.add(TransportToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        CheckBox1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_True01.png"))); // NOI18N
        CheckBox1.setFocusPainted(false);
        CheckBox1.setFocusable(false);
        CheckBox1.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox1.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox1.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox1MouseExited(evt);
            }
        });
        CheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox1ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 44, -1, -1));

        CheckBox2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_True01.png"))); // NOI18N
        CheckBox2.setFocusPainted(false);
        CheckBox2.setFocusable(false);
        CheckBox2.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox2.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox2.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox2MouseExited(evt);
            }
        });
        CheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox2ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(659, 44, -1, -1));

        CheckBox3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_True01.png"))); // NOI18N
        CheckBox3.setFocusPainted(false);
        CheckBox3.setFocusable(false);
        CheckBox3.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox3.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox3.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox3MouseExited(evt);
            }
        });
        CheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox3ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(699, 44, -1, -1));

        CheckBox4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_True01.png"))); // NOI18N
        CheckBox4.setFocusPainted(false);
        CheckBox4.setFocusable(false);
        CheckBox4.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox4.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox4.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox4MouseExited(evt);
            }
        });
        CheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox4ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(739, 44, -1, -1));

        CheckBox5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox5.setFocusPainted(false);
        CheckBox5.setFocusable(false);
        CheckBox5.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox5.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox5.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox5MouseExited(evt);
            }
        });
        CheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox5ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(779, 44, -1, -1));

        CheckBox6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox6.setFocusPainted(false);
        CheckBox6.setFocusable(false);
        CheckBox6.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox6.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox6.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox6MouseExited(evt);
            }
        });
        CheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox6ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 60, -1, -1));

        CheckBox7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox7.setFocusPainted(false);
        CheckBox7.setFocusable(false);
        CheckBox7.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox7.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox7.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox7MouseExited(evt);
            }
        });
        CheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox7ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(659, 60, -1, -1));

        CheckBox8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox8.setFocusPainted(false);
        CheckBox8.setFocusable(false);
        CheckBox8.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox8.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox8.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox8MouseExited(evt);
            }
        });
        CheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox8ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(699, 60, -1, -1));

        CheckBox9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox9.setFocusPainted(false);
        CheckBox9.setFocusable(false);
        CheckBox9.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox9.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox9.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox9MouseExited(evt);
            }
        });
        CheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox9ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox9, new org.netbeans.lib.awtextra.AbsoluteConstraints(739, 60, -1, -1));

        CheckBox10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CheckBox_False01.png"))); // NOI18N
        CheckBox10.setFocusPainted(false);
        CheckBox10.setFocusable(false);
        CheckBox10.setMaximumSize(new java.awt.Dimension(14, 14));
        CheckBox10.setMinimumSize(new java.awt.Dimension(14, 14));
        CheckBox10.setPreferredSize(new java.awt.Dimension(14, 14));
        CheckBox10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CheckBox10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CheckBox10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CheckBox10MouseExited(evt);
            }
        });
        CheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBox10ActionPerformed(evt);
            }
        });
        statusBar.add(CheckBox10, new org.netbeans.lib.awtextra.AbsoluteConstraints(779, 60, -1, -1));

        RecordToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/RecordToolBar.png"))); // NOI18N
        statusBar.add(RecordToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, -1, -1));

        statusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(1, 209, 255));
        statusBar.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, -1, -1));

        fileNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        fileNameLabel.setForeground(new java.awt.Color(1, 209, 255));
        statusBar.add(fileNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 5, -1, -1));

        btn_min.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_min.png"))); // NOI18N
        btn_min.setBorderPainted(false);
        btn_min.setFocusPainted(false);
        btn_min.setFocusable(false);
        btn_min.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_minMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_minMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_minMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_minMouseReleased(evt);
            }
        });
        btn_min.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_minActionPerformed(evt);
            }
        });
        statusBar.add(btn_min, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 5, 14, 15));

        btn_max.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_max.png"))); // NOI18N
        btn_max.setBorderPainted(false);
        btn_max.setFocusPainted(false);
        btn_max.setFocusable(false);
        btn_max.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_maxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_maxMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_maxMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_maxMouseReleased(evt);
            }
        });
        statusBar.add(btn_max, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 5, 14, 15));

        btn_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_close.png"))); // NOI18N
        btn_close.setBorderPainted(false);
        btn_close.setFocusPainted(false);
        btn_close.setFocusable(false);
        btn_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_closeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_closeMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_closeMouseReleased(evt);
            }
        });
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        statusBar.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 5, 14, 15));

        btn_syncA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_syncA_01.png"))); // NOI18N
        btn_syncA.setBorderPainted(false);
        btn_syncA.setFocusPainted(false);
        btn_syncA.setFocusable(false);
        btn_syncA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_syncAMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_syncAMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_syncAMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_syncAMouseReleased(evt);
            }
        });
        statusBar.add(btn_syncA, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 66, 18, 11));

        btn_syncB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_syncB_01.png"))); // NOI18N
        btn_syncB.setBorderPainted(false);
        btn_syncB.setFocusPainted(false);
        btn_syncB.setFocusable(false);
        btn_syncB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_syncBMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_syncBMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_syncBMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_syncBMouseReleased(evt);
            }
        });
        statusBar.add(btn_syncB, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 55, 18, 11));

        btn_file.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuFile.png"))); // NOI18N
        btn_file.setBorderPainted(false);
        btn_file.setFocusPainted(false);
        btn_file.setFocusable(false);
        btn_file.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_fileMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_fileMouseReleased(evt);
            }
        });
        btn_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_fileActionPerformed(evt);
            }
        });
        statusBar.add(btn_file, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 30, 32, 19));

        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuEdit.png"))); // NOI18N
        btn_edit.setBorderPainted(false);
        btn_edit.setFocusPainted(false);
        btn_edit.setFocusable(false);
        btn_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_editMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_editMouseReleased(evt);
            }
        });
        statusBar.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 30, 30, 19));

        btn_channels.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuChannels.png"))); // NOI18N
        btn_channels.setToolTipText("");
        btn_channels.setBorderPainted(false);
        btn_channels.setFocusPainted(false);
        btn_channels.setFocusable(false);
        btn_channels.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_channelsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_channelsMouseReleased(evt);
            }
        });
        statusBar.add(btn_channels, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 30, 57, 19));

        btn_view.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuView.png"))); // NOI18N
        btn_view.setBorderPainted(false);
        btn_view.setFocusPainted(false);
        btn_view.setFocusable(false);
        btn_view.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewMouseReleased(evt);
            }
        });
        statusBar.add(btn_view, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 30, 36, 19));

        btn_options.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuOptions.png"))); // NOI18N
        btn_options.setBorderPainted(false);
        btn_options.setFocusPainted(false);
        btn_options.setFocusable(false);
        btn_options.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_optionsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_optionsMouseReleased(evt);
            }
        });
        statusBar.add(btn_options, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 30, 50, 19));

        btn_tools.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuTools.png"))); // NOI18N
        btn_tools.setBorderPainted(false);
        btn_tools.setFocusPainted(false);
        btn_tools.setFocusable(false);
        btn_tools.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_toolsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_toolsMouseReleased(evt);
            }
        });
        statusBar.add(btn_tools, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 30, 40, 19));

        btn_help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_menuHelp.png"))); // NOI18N
        btn_help.setBorderPainted(false);
        btn_help.setFocusPainted(false);
        btn_help.setFocusable(false);
        btn_help.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_helpMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_helpMouseReleased(evt);
            }
        });
        statusBar.add(btn_help, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 30, 35, 19));

        ControlToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/ControlToolBar.png"))); // NOI18N
        statusBar.add(ControlToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        MonitorToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/MonitorToolBar.png"))); // NOI18N
        statusBar.add(MonitorToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, -1, -1));

        MemMeterFill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/MemMeter_fill.png"))); // NOI18N
        MemMeterFill.setDoubleBuffered(true);
        statusBar.add(MemMeterFill, new org.netbeans.lib.awtextra.AbsoluteConstraints(818, 3, 0, -1));

        MemMeter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/MemMeter_empty.png"))); // NOI18N
        statusBar.add(MemMeter, new org.netbeans.lib.awtextra.AbsoluteConstraints(818, 3, 30, -1));

        RAMDigit4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(RAMDigit4, new org.netbeans.lib.awtextra.AbsoluteConstraints(857, 5, -1, -1));

        RAMDigit3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(RAMDigit3, new org.netbeans.lib.awtextra.AbsoluteConstraints(863, 5, -1, -1));

        RAMDigit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(RAMDigit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(869, 5, -1, -1));

        RAMDigit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CPUDigits9_0.png"))); // NOI18N
        statusBar.add(RAMDigit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 5, -1, -1));

        CPUPolyDigit4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(CPUPolyDigit4, new org.netbeans.lib.awtextra.AbsoluteConstraints(857, 25, -1, -1));

        CPUPolyDigit3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(CPUPolyDigit3, new org.netbeans.lib.awtextra.AbsoluteConstraints(863, 25, -1, -1));

        CPUPolyDigit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(CPUPolyDigit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(869, 25, -1, -1));

        CPUPolyDigit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CPUDigits9_0.png"))); // NOI18N
        statusBar.add(CPUPolyDigit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 25, -1, -1));

        CPUDigit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/none_number.png"))); // NOI18N
        statusBar.add(CPUDigit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(803, 5, -1, -1));

        CPUDigit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CPUDigits9_0.png"))); // NOI18N
        statusBar.add(CPUDigit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(809, 5, -1, -1));

        CPUMeter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CPUMeter_empty.png"))); // NOI18N
        statusBar.add(CPUMeter, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 5, 72, -1));

        CPUToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/CPUToolBar.png"))); // NOI18N
        statusBar.add(CPUToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, -1, -1));

        btn_viewPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_viewPlay.png"))); // NOI18N
        btn_viewPlay.setBorderPainted(false);
        btn_viewPlay.setFocusPainted(false);
        btn_viewPlay.setFocusable(false);
        btn_viewPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_viewPlayMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_viewPlayMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewPlayMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewPlayMouseReleased(evt);
            }
        });
        statusBar.add(btn_viewPlay, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 5, 28, 28));

        btn_viewStep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_viewStep.png"))); // NOI18N
        btn_viewStep.setBorderPainted(false);
        btn_viewStep.setFocusPainted(false);
        btn_viewStep.setFocusable(false);
        btn_viewStep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_viewStepMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_viewStepMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewStepMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewStepMouseReleased(evt);
            }
        });
        statusBar.add(btn_viewStep, new org.netbeans.lib.awtextra.AbsoluteConstraints(929, 5, 28, 28));

        btn_viewPiano.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_viewPiano.png"))); // NOI18N
        btn_viewPiano.setBorderPainted(false);
        btn_viewPiano.setFocusPainted(false);
        btn_viewPiano.setFocusable(false);
        btn_viewPiano.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_viewPianoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_viewPianoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewPianoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewPianoMouseReleased(evt);
            }
        });
        btn_viewPiano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_viewPianoActionPerformed(evt);
            }
        });
        statusBar.add(btn_viewPiano, new org.netbeans.lib.awtextra.AbsoluteConstraints(958, 5, 28, 28));

        btn_viewBrowser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_viewBrowser.png"))); // NOI18N
        btn_viewBrowser.setBorderPainted(false);
        btn_viewBrowser.setFocusPainted(false);
        btn_viewBrowser.setFocusable(false);
        btn_viewBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_viewBrowserMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_viewBrowserMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewBrowserMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewBrowserMouseReleased(evt);
            }
        });
        statusBar.add(btn_viewBrowser, new org.netbeans.lib.awtextra.AbsoluteConstraints(987, 5, 28, 28));

        btn_viewMixer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_viewMixer.png"))); // NOI18N
        btn_viewMixer.setBorderPainted(false);
        btn_viewMixer.setFocusPainted(false);
        btn_viewMixer.setFocusable(false);
        btn_viewMixer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_viewMixerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_viewMixerMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_viewMixerMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_viewMixerMouseReleased(evt);
            }
        });
        statusBar.add(btn_viewMixer, new org.netbeans.lib.awtextra.AbsoluteConstraints(1016, 5, 28, 28));

        ShortcutToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/ShortcutToolBar.png"))); // NOI18N
        statusBar.add(ShortcutToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 0, -1, -1));

        btn_undo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_undo.png"))); // NOI18N
        btn_undo.setBorderPainted(false);
        btn_undo.setFocusPainted(false);
        btn_undo.setFocusable(false);
        btn_undo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_undoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_undoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_undoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_undoMouseReleased(evt);
            }
        });
        statusBar.add(btn_undo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 5, 28, 28));

        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_save.png"))); // NOI18N
        btn_save.setBorderPainted(false);
        btn_save.setFocusPainted(false);
        btn_save.setFocusable(false);
        btn_save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_saveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_saveMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_saveMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_saveMouseReleased(evt);
            }
        });
        statusBar.add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(1089, 5, 28, 28));

        btn_render.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_render.png"))); // NOI18N
        btn_render.setBorderPainted(false);
        btn_render.setFocusPainted(false);
        btn_render.setFocusable(false);
        btn_render.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_renderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_renderMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_renderMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_renderMouseReleased(evt);
            }
        });
        statusBar.add(btn_render, new org.netbeans.lib.awtextra.AbsoluteConstraints(1118, 5, 28, 28));

        btn_audioEditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_audioEditor.png"))); // NOI18N
        btn_audioEditor.setBorderPainted(false);
        btn_audioEditor.setFocusPainted(false);
        btn_audioEditor.setFocusable(false);
        btn_audioEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_audioEditorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_audioEditorMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_audioEditorMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_audioEditorMouseReleased(evt);
            }
        });
        statusBar.add(btn_audioEditor, new org.netbeans.lib.awtextra.AbsoluteConstraints(1147, 5, 28, 28));

        btn_recording.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_recording.png"))); // NOI18N
        btn_recording.setBorderPainted(false);
        btn_recording.setFocusPainted(false);
        btn_recording.setFocusable(false);
        btn_recording.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_recordingMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_recordingMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_recordingMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_recordingMouseReleased(evt);
            }
        });
        statusBar.add(btn_recording, new org.netbeans.lib.awtextra.AbsoluteConstraints(1176, 5, 28, 28));

        btn_info.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_info.png"))); // NOI18N
        btn_info.setBorderPainted(false);
        btn_info.setFocusPainted(false);
        btn_info.setFocusable(false);
        btn_info.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_infoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_infoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_infoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_infoMouseReleased(evt);
            }
        });
        statusBar.add(btn_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(1205, 5, 28, 28));

        btn_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_about.png"))); // NOI18N
        btn_about.setBorderPainted(false);
        btn_about.setFocusPainted(false);
        btn_about.setFocusable(false);
        btn_about.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_aboutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_aboutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_aboutMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_aboutMouseReleased(evt);
            }
        });
        statusBar.add(btn_about, new org.netbeans.lib.awtextra.AbsoluteConstraints(1234, 5, 28, 28));

        ShortcutToolBar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/ShortcutToolBar2.png"))); // NOI18N
        statusBar.add(ShortcutToolBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 0, -1, -1));

        Copyright.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Copyright.setForeground(new java.awt.Color(255, 255, 255));
        Copyright.setText("Created by LGY (12151430)");
        statusBar.add(Copyright, new org.netbeans.lib.awtextra.AbsoluteConstraints(915, 45, -1, -1));

        OnlineToolBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/OnlineToolBar.png"))); // NOI18N
        statusBar.add(OnlineToolBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 40, -1, -1));

        backgroundPanel.setBackground(new java.awt.Color(81, 86, 88));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/background.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout backgroundPanelLayout = new org.jdesktop.layout.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backgroundPanelLayout.createSequentialGroup()
                .add(background)
                .add(0, 16334, Short.MAX_VALUE))
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backgroundPanelLayout.createSequentialGroup()
                .add(background)
                .add(0, 2031, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(47, 47, 47));

        projectPanel.setBackground(new java.awt.Color(39, 40, 34));
        projectPanel.setOpaque(true);
        projectPanel.setPreferredSize(new java.awt.Dimension(12000, 1600));
        projectPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                projectPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                projectPanelMouseMoved(evt);
            }
        });

        org.jdesktop.layout.GroupLayout projectPanelLayout = new org.jdesktop.layout.GroupLayout(projectPanel);
        projectPanel.setLayout(projectPanelLayout);
        projectPanelLayout.setHorizontalGroup(
            projectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 12000, Short.MAX_VALUE)
        );
        projectPanelLayout.setVerticalGroup(
            projectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1600, Short.MAX_VALUE)
        );

        projectScrollPane.setViewportView(projectPanel);

        trackInfoBar.setBackground(new java.awt.Color(0, 0, 0));
        trackInfoBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        trackInfoBar.setPreferredSize(new java.awt.Dimension(491, 1080));
        trackInfoBar.setLayout(null);

        trackRightBorder.setBackground(new java.awt.Color(63, 72, 77));
        trackRightBorder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 105, 110)));

        org.jdesktop.layout.GroupLayout trackRightBorderLayout = new org.jdesktop.layout.GroupLayout(trackRightBorder);
        trackRightBorder.setLayout(trackRightBorderLayout);
        trackRightBorderLayout.setHorizontalGroup(
            trackRightBorderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 11, Short.MAX_VALUE)
        );
        trackRightBorderLayout.setVerticalGroup(
            trackRightBorderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        trackInfoBar.add(trackRightBorder);
        trackRightBorder.setBounds(218, 1, 13, 1985);

        trackLeftBorder.setBackground(new java.awt.Color(32, 41, 46));
        trackLeftBorder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(27, 35, 39)));
        trackLeftBorder.setForeground(new java.awt.Color(255, 255, 255));
        trackLeftBorder.setFont(new java.awt.Font("굴림", 0, 16)); // NOI18N

        org.jdesktop.layout.GroupLayout trackLeftBorderLayout = new org.jdesktop.layout.GroupLayout(trackLeftBorder);
        trackLeftBorder.setLayout(trackLeftBorderLayout);
        trackLeftBorderLayout.setHorizontalGroup(
            trackLeftBorderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 9, Short.MAX_VALUE)
        );
        trackLeftBorderLayout.setVerticalGroup(
            trackLeftBorderLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        trackInfoBar.add(trackLeftBorder);
        trackLeftBorder.setBounds(1, 21, 11, 1965);

        trackWindowPanel.setBackground(new java.awt.Color(95, 104, 109));
        trackWindowPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_browserOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_browserOptions.png"))); // NOI18N
        btn_browserOptions.setBorderPainted(false);
        btn_browserOptions.setFocusPainted(false);
        btn_browserOptions.setFocusable(false);
        btn_browserOptions.setPreferredSize(new java.awt.Dimension(49, 25));
        btn_browserOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_browserOptionsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_browserOptionsMouseExited(evt);
            }
        });

        btn_collapseStructure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_collapseStructure.png"))); // NOI18N
        btn_collapseStructure.setBorderPainted(false);
        btn_collapseStructure.setFocusPainted(false);
        btn_collapseStructure.setFocusable(false);
        btn_collapseStructure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_collapseStructureMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_collapseStructureMouseExited(evt);
            }
        });

        btn_refreshStructure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_refreshStructure.png"))); // NOI18N
        btn_refreshStructure.setBorderPainted(false);
        btn_refreshStructure.setFocusPainted(false);
        btn_refreshStructure.setFocusable(false);
        btn_refreshStructure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_refreshStructureMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_refreshStructureMouseExited(evt);
            }
        });

        trackWindow.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        trackWindow.setForeground(new java.awt.Color(143, 152, 155));
        trackWindow.setText("Track Window");

        btn_closeStructure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_closeStructure.png"))); // NOI18N
        btn_closeStructure.setBorderPainted(false);
        btn_closeStructure.setFocusPainted(false);
        btn_closeStructure.setFocusable(false);
        btn_closeStructure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_closeStructureMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_closeStructureMouseExited(evt);
            }
        });

        org.jdesktop.layout.GroupLayout trackWindowPanelLayout = new org.jdesktop.layout.GroupLayout(trackWindowPanel);
        trackWindowPanel.setLayout(trackWindowPanelLayout);
        trackWindowPanelLayout.setHorizontalGroup(
            trackWindowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackWindowPanelLayout.createSequentialGroup()
                .add(btn_browserOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_collapseStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_refreshStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(trackWindow)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 54, Short.MAX_VALUE)
                .add(btn_closeStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        trackWindowPanelLayout.setVerticalGroup(
            trackWindowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(btn_browserOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(btn_collapseStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(btn_refreshStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(trackWindowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(btn_closeStructure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(trackWindow))
        );

        trackInfoBar.add(trackWindowPanel);
        trackWindowPanel.setBounds(1, 1, 215, 19);

        trackNamePanel.setBackground(new java.awt.Color(21, 30, 35));
        trackNamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        trackNameLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        trackNameLabel.setForeground(new java.awt.Color(183, 144, 111));
        trackNameLabel.setText("Staff-01");

        trackMixerIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/trackMixerIcon.png"))); // NOI18N

        trackNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        trackNumberLabel.setForeground(new java.awt.Color(240, 240, 240));
        trackNumberLabel.setText("1");

        trackNameMarker.setBackground(new java.awt.Color(51, 60, 65));
        trackNameMarker.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        trackRightArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/rightArrowIcon.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout trackNameMarkerLayout = new org.jdesktop.layout.GroupLayout(trackNameMarker);
        trackNameMarker.setLayout(trackNameMarkerLayout);
        trackNameMarkerLayout.setHorizontalGroup(
            trackNameMarkerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, trackNameMarkerLayout.createSequentialGroup()
                .add(5, 5, 5)
                .add(trackRightArrow)
                .add(5, 5, 5))
        );
        trackNameMarkerLayout.setVerticalGroup(
            trackNameMarkerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackNameMarkerLayout.createSequentialGroup()
                .add(15, 15, 15)
                .add(trackRightArrow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15))
        );

        org.jdesktop.layout.GroupLayout trackNamePanelLayout = new org.jdesktop.layout.GroupLayout(trackNamePanel);
        trackNamePanel.setLayout(trackNamePanelLayout);
        trackNamePanelLayout.setHorizontalGroup(
            trackNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackNamePanelLayout.createSequentialGroup()
                .add(0, 0, 0)
                .add(trackNameMarker, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(trackNumberLabel)
                .add(18, 18, 18)
                .add(trackNameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 61, Short.MAX_VALUE)
                .add(trackMixerIcon)
                .addContainerGap())
        );
        trackNamePanelLayout.setVerticalGroup(
            trackNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(trackNamePanelLayout.createSequentialGroup()
                .add(11, 11, 11)
                .add(trackNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(trackNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(trackNumberLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(trackNameLabel))
                    .add(trackMixerIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(13, 13, 13))
            .add(trackNameMarker, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        trackInfoBar.add(trackNamePanel);
        trackNamePanel.setBounds(14, 22, 202, 50);

        volumeIconLabel.setForeground(new java.awt.Color(1, 209, 255));
        volumeIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/volumeIcon.png"))); // NOI18N
        trackInfoBar.add(volumeIconLabel);
        volumeIconLabel.setBounds(30, 100, 10, 9);

        volumeFillLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/volumeFill.png"))); // NOI18N
        trackInfoBar.add(volumeFillLabel);
        volumeFillLabel.setBounds(60, 88, 50, 30);

        volumeEmptyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/volumeEmpty.png"))); // NOI18N
        trackInfoBar.add(volumeEmptyLabel);
        volumeEmptyLabel.setBounds(60, 90, 80, 28);

        volumeValueLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        volumeValueLabel.setForeground(new java.awt.Color(1, 209, 255));
        volumeValueLabel.setText("0 dB");
        trackInfoBar.add(volumeValueLabel);
        volumeValueLabel.setBounds(150, 88, 56, 30);

        panelBorder1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panelBorder.png"))); // NOI18N
        trackInfoBar.add(panelBorder1);
        panelBorder1.setBounds(20, 90, 190, 30);

        panIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panIcon.png"))); // NOI18N
        trackInfoBar.add(panIconLabel);
        panIconLabel.setBounds(30, 130, 11, 10);

        panFillLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/volumeFill.png"))); // NOI18N
        trackInfoBar.add(panFillLabel);
        panFillLabel.setBounds(93, 120, 3, 30);

        panValueLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        panValueLabel.setForeground(new java.awt.Color(1, 209, 255));
        panValueLabel.setText("C");
        trackInfoBar.add(panValueLabel);
        panValueLabel.setBounds(150, 119, 56, 30);

        panEmptyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/volumeEmpty.png"))); // NOI18N
        trackInfoBar.add(panEmptyLabel);
        panEmptyLabel.setBounds(60, 120, 80, 30);

        panelBorder2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panelBorder.png"))); // NOI18N
        trackInfoBar.add(panelBorder2);
        panelBorder2.setBounds(20, 119, 190, 30);

        midiInputIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/midiInputIcon.png"))); // NOI18N
        trackInfoBar.add(midiInputIconLabel);
        midiInputIconLabel.setBounds(30, 190, 20, 11);

        midiInputLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        midiInputLabel.setForeground(new java.awt.Color(1, 209, 255));
        midiInputLabel.setText("All MIDI Inputs");
        trackInfoBar.add(midiInputLabel);
        midiInputLabel.setBounds(50, 180, 120, 28);

        panelBorder3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panelBorder.png"))); // NOI18N
        trackInfoBar.add(panelBorder3);
        panelBorder3.setBounds(20, 180, 190, 30);

        instIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/instIcon.png"))); // NOI18N
        trackInfoBar.add(instIconLabel);
        instIconLabel.setBounds(30, 220, 10, 9);

        instLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        instLabel.setForeground(new java.awt.Color(1, 209, 255));
        instLabel.setText("<None>");
        trackInfoBar.add(instLabel);
        instLabel.setBounds(50, 208, 150, 30);

        panelBorder4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panelBorder.png"))); // NOI18N
        panelBorder4.setText("jLabel4");
        trackInfoBar.add(panelBorder4);
        panelBorder4.setBounds(20, 209, 190, 30);

        channelIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/channelIcon.png"))); // NOI18N
        trackInfoBar.add(channelIconLabel);
        channelIconLabel.setBounds(30, 238, 20, 30);

        channelLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        channelLabel.setForeground(new java.awt.Color(1, 209, 255));
        channelLabel.setText("Channel 0");
        trackInfoBar.add(channelLabel);
        channelLabel.setBounds(50, 238, 150, 30);

        panelBorder5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/panelBorder.png"))); // NOI18N
        trackInfoBar.add(panelBorder5);
        panelBorder5.setBounds(20, 238, 190, 30);

        pianoRollPanel.setBackground(new java.awt.Color(21, 30, 35));
        pianoRollPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        pianoRollLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pianoRollLabel.setForeground(new java.awt.Color(112, 149, 174));
        pianoRollLabel.setText("Piano Roll");

        pianoRollIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/pianoRollIcon.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout pianoRollPanelLayout = new org.jdesktop.layout.GroupLayout(pianoRollPanel);
        pianoRollPanel.setLayout(pianoRollPanelLayout);
        pianoRollPanelLayout.setHorizontalGroup(
            pianoRollPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pianoRollPanelLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(pianoRollLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(pianoRollIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        pianoRollPanelLayout.setVerticalGroup(
            pianoRollPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pianoRollLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
            .add(pianoRollIcon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        trackInfoBar.add(pianoRollPanel);
        pianoRollPanel.setBounds(20, 300, 190, 30);

        eventListPanel.setBackground(new java.awt.Color(21, 30, 35));
        eventListPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        eventListLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        eventListLabel.setForeground(new java.awt.Color(112, 149, 174));
        eventListLabel.setText("Event List");

        eventListIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/eventListIcon.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout eventListPanelLayout = new org.jdesktop.layout.GroupLayout(eventListPanel);
        eventListPanel.setLayout(eventListPanelLayout);
        eventListPanelLayout.setHorizontalGroup(
            eventListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(eventListPanelLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(eventListLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                .add(eventListIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5))
        );
        eventListPanelLayout.setVerticalGroup(
            eventListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(eventListLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
            .add(eventListIcon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        trackInfoBar.add(eventListPanel);
        eventListPanel.setBounds(20, 332, 190, 30);

        insertsPanel.setBackground(new java.awt.Color(21, 30, 35));
        insertsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        insertsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        insertsLabel.setForeground(new java.awt.Color(112, 149, 174));
        insertsLabel.setText("Inserts");

        insertsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/insertsIcon.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout insertsPanelLayout = new org.jdesktop.layout.GroupLayout(insertsPanel);
        insertsPanel.setLayout(insertsPanelLayout);
        insertsPanelLayout.setHorizontalGroup(
            insertsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(insertsPanelLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(insertsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 36, Short.MAX_VALUE)
                .add(insertsIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        insertsPanelLayout.setVerticalGroup(
            insertsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(insertsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
            .add(insertsIcon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        trackInfoBar.add(insertsPanel);
        insertsPanel.setBounds(20, 364, 190, 30);

        sendsPanel.setBackground(new java.awt.Color(21, 30, 35));
        sendsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        sendsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sendsLabel.setForeground(new java.awt.Color(112, 149, 174));
        sendsLabel.setText("Sends");

        sendsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/sendsIcon.png"))); // NOI18N
        sendsIcon.setText("jLabel9");

        org.jdesktop.layout.GroupLayout sendsPanelLayout = new org.jdesktop.layout.GroupLayout(sendsPanel);
        sendsPanel.setLayout(sendsPanelLayout);
        sendsPanelLayout.setHorizontalGroup(
            sendsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sendsPanelLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(sendsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(sendsIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        sendsPanelLayout.setVerticalGroup(
            sendsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sendsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(sendsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .add(sendsIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        trackInfoBar.add(sendsPanel);
        sendsPanel.setBounds(20, 396, 190, 30);

        keyboardPanel.setBackground(new java.awt.Color(21, 30, 35));
        keyboardPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 60, 65)));

        keyboardLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        keyboardLabel.setForeground(new java.awt.Color(112, 149, 174));
        keyboardLabel.setText("Keyboard");
        keyboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                keyboardLabelMousePressed(evt);
            }
        });

        keyboardIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/keyboardIcon.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout keyboardPanelLayout = new org.jdesktop.layout.GroupLayout(keyboardPanel);
        keyboardPanel.setLayout(keyboardPanelLayout);
        keyboardPanelLayout.setHorizontalGroup(
            keyboardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(keyboardPanelLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(keyboardLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(keyboardIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        keyboardPanelLayout.setVerticalGroup(
            keyboardPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(keyboardLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
            .add(keyboardIcon, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        trackInfoBar.add(keyboardPanel);
        keyboardPanel.setBounds(20, 428, 190, 30);

        trackScrollPane.setBackground(new java.awt.Color(71, 72, 74));
        trackScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        trackScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        InstrumentBar.setBackground(new java.awt.Color(63, 72, 77));
        InstrumentBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 105, 110)));
        InstrumentBar.setPreferredSize(new java.awt.Dimension(600, 1600));

        org.jdesktop.layout.GroupLayout InstrumentBarLayout = new org.jdesktop.layout.GroupLayout(InstrumentBar);
        InstrumentBar.setLayout(InstrumentBarLayout);
        InstrumentBarLayout.setHorizontalGroup(
            InstrumentBarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 598, Short.MAX_VALUE)
        );
        InstrumentBarLayout.setVerticalGroup(
            InstrumentBarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1598, Short.MAX_VALUE)
        );

        trackScrollPane.setViewportView(InstrumentBar);

        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        PLToolbarPanel.setBackground(new java.awt.Color(95, 104, 109));
        PLToolbarPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_playListOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_playListOptions.png"))); // NOI18N
        btn_playListOptions.setBorderPainted(false);
        btn_playListOptions.setFocusPainted(false);
        btn_playListOptions.setFocusable(false);
        btn_playListOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_playListOptionsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_playListOptionsMouseExited(evt);
            }
        });

        btn_snapToGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_snapToGrid.png"))); // NOI18N
        btn_snapToGrid.setBorderPainted(false);
        btn_snapToGrid.setFocusPainted(false);
        btn_snapToGrid.setFocusable(false);
        btn_snapToGrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_snapToGridMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_snapToGridMouseExited(evt);
            }
        });

        btn_draw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_draw.png"))); // NOI18N
        btn_draw.setBorderPainted(false);
        btn_draw.setFocusPainted(false);
        btn_draw.setFocusable(false);
        btn_draw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_drawMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_drawMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_drawMousePressed(evt);
            }
        });

        btn_paint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_paint.png"))); // NOI18N
        btn_paint.setBorderPainted(false);
        btn_paint.setFocusPainted(false);
        btn_paint.setFocusable(false);
        btn_paint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_paintMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_paintMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_paintMousePressed(evt);
            }
        });

        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_delete.png"))); // NOI18N
        btn_delete.setBorderPainted(false);
        btn_delete.setFocusPainted(false);
        btn_delete.setFocusable(false);
        btn_delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_deleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_deleteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_deleteMousePressed(evt);
            }
        });

        btn_mute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_mute.png"))); // NOI18N
        btn_mute.setBorderPainted(false);
        btn_mute.setFocusPainted(false);
        btn_mute.setFocusable(false);
        btn_mute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_muteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_muteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_muteMousePressed(evt);
            }
        });

        btn_slip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_slip.png"))); // NOI18N
        btn_slip.setBorderPainted(false);
        btn_slip.setFocusPainted(false);
        btn_slip.setFocusable(false);
        btn_slip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_slipMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_slipMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_slipMousePressed(evt);
            }
        });

        btn_slice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_slice.png"))); // NOI18N
        btn_slice.setBorderPainted(false);
        btn_slice.setFocusPainted(false);
        btn_slice.setFocusable(false);
        btn_slice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_sliceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_sliceMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_sliceMousePressed(evt);
            }
        });

        btn_select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_select.png"))); // NOI18N
        btn_select.setBorderPainted(false);
        btn_select.setFocusPainted(false);
        btn_select.setFocusable(false);
        btn_select.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_selectMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_selectMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_selectMousePressed(evt);
            }
        });

        btn_zoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_zoom.png"))); // NOI18N
        btn_zoom.setBorderPainted(false);
        btn_zoom.setFocusPainted(false);
        btn_zoom.setFocusable(false);
        btn_zoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_zoomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_zoomMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_zoomMousePressed(evt);
            }
        });

        btn_playback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/btn_playback.png"))); // NOI18N
        btn_playback.setBorderPainted(false);
        btn_playback.setFocusPainted(false);
        btn_playback.setFocusable(false);
        btn_playback.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_playbackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_playbackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_playbackMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_playbackMousePressed(evt);
            }
        });
        btn_playback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_playbackActionPerformed(evt);
            }
        });

        btn_EEPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MusicTree/resources/EEPlay.png"))); // NOI18N
        btn_EEPlay.setBorderPainted(false);
        btn_EEPlay.setFocusPainted(false);
        btn_EEPlay.setFocusable(false);
        btn_EEPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_EEPlayMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_EEPlayMouseExited(evt);
            }
        });

        playListLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        playListLabel.setForeground(new java.awt.Color(143, 152, 155));
        playListLabel.setText("Playlist - ");

        org.jdesktop.layout.GroupLayout PLToolbarPanelLayout = new org.jdesktop.layout.GroupLayout(PLToolbarPanel);
        PLToolbarPanel.setLayout(PLToolbarPanelLayout);
        PLToolbarPanelLayout.setHorizontalGroup(
            PLToolbarPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PLToolbarPanelLayout.createSequentialGroup()
                .add(1, 1, 1)
                .add(btn_playListOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_snapToGrid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(btn_draw, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_paint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_delete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_mute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_slip, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_slice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_select, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_zoom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(btn_playback, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btn_EEPlay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(playListLabel)
                .addContainerGap(1404, Short.MAX_VALUE))
        );
        PLToolbarPanelLayout.setVerticalGroup(
            PLToolbarPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(PLToolbarPanelLayout.createSequentialGroup()
                .add(1, 1, 1)
                .add(PLToolbarPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(playListLabel)
                    .add(btn_EEPlay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_playback, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_zoom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_select, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_slice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_slip, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_mute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_delete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_paint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_draw, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_snapToGrid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btn_playListOptions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(1, 1, 1))
        );

        timeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        timeScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        timeBar.setBackground(new java.awt.Color(11, 27, 37));
        timeBar.setOpaque(true);
        timeBar.setPreferredSize(new java.awt.Dimension(24000, 100));

        timeNumLabel.setFont(new java.awt.Font("MS Gothic", 0, 14)); // NOI18N
        timeNumLabel.setForeground(new java.awt.Color(138, 150, 161));
        timeNumLabel.setMaximumSize(new java.awt.Dimension(32767, 0));
        timeNumLabel.setPreferredSize(new java.awt.Dimension(12000, 0));

        timeBar.setLayer(timeNumLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        org.jdesktop.layout.GroupLayout timeBarLayout = new org.jdesktop.layout.GroupLayout(timeBar);
        timeBar.setLayout(timeBarLayout);
        timeBarLayout.setHorizontalGroup(
            timeBarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(timeNumLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        timeBarLayout.setVerticalGroup(
            timeBarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(timeBarLayout.createSequentialGroup()
                .add(timeNumLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 74, Short.MAX_VALUE))
        );

        timeScrollPane.setViewportView(timeBar);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(trackInfoBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 234, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(PLToolbarPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(trackScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(2, 2, 2)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(timeScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(projectScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1575, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 0, Short.MAX_VALUE))))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(546, 546, 546)
                                .add(jFileChooser1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(5538, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(PLToolbarPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1)
                        .add(timeScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(trackScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(projectScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 917, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 523, Short.MAX_VALUE)
                        .add(jFileChooser1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(82, 82, 82))
                    .add(trackInfoBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 2002, Short.MAX_VALUE))
                .add(39, 39, 39))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(border_top)
                    .add(layout.createSequentialGroup()
                        .add(border_left)
                        .add(0, 0, 0)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(statusBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(backgroundPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(border_top)
                .add(0, 0, 0)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(statusBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backgroundPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(border_left))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
        
        if (projectStart) {
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportPlay.png"));
                imgEEPlay = ImageIO.read(getClass().getResource("resources/EEPlay.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            btn_EEPlay.setIcon(new ImageIcon(imgEEPlay));
            playButton.setIcon(new ImageIcon(img));
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportStop.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            stopButton.setIcon(new ImageIcon(img));

            timer.reset();
            UpdateOffset.setMusicPlay(false);
            UpdateOffset.setTimer(0);
            for (int j = 0; j < VVstiTrack.size(); j++) {
                VVstiTrack.get(j).resetTimer();
            }
          //  WavData[0].stopWav();
          //  WavData[0].setPosition(0);
//            openFile.stopSoundPlayer();
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        
        if (projectStart) {
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportPlay.png"));
                imgEEPlay = ImageIO.read(getClass().getResource("resources/EEPlay.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            btn_EEPlay.setIcon(new ImageIcon(imgEEPlay));
            playButton.setIcon(new ImageIcon(img));
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportStop2.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            stopButton.setIcon(new ImageIcon(img));
            UpdateOffset.setMusicPlay(false);
            timer.pause();

            openFile.pauseSoundPlayer();
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        // TODO add your handling code here:
        
        if (projectStart) {
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportPlay2.png"));
                imgEEPlay = ImageIO.read(getClass().getResource("resources/EEPlay2.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            btn_EEPlay.setIcon(new ImageIcon(imgEEPlay));
            playButton.setIcon(new ImageIcon(img));
            try {
                img = ImageIO.read(getClass().getResource("resources/TransportStop.png"));
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            stopButton.setIcon(new ImageIcon(img));
            UpdateOffset.setMusicPlay(true);
            timer.play();
            openFile.playSoundPlayer();
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void resetButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resetButtonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonKeyPressed

    private void CheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox1ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[0]) {
            recordToolBarCheckBox[0] = false;
            CheckBox1.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[0] = true;
            CheckBox1.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox1ActionPerformed

    private void CheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox2ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[1]) {
            recordToolBarCheckBox[1] = false;
            CheckBox2.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[1] = true;
            CheckBox2.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox2ActionPerformed

    private void CheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox3ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[2]) {
            recordToolBarCheckBox[2] = false;
            CheckBox3.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[2] = true;
            CheckBox3.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox3ActionPerformed

    private void CheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox4ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[3]) {
            recordToolBarCheckBox[3] = false;
            CheckBox4.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[3] = true;
            CheckBox4.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox4ActionPerformed

    private void CheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox5ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[4]) {
            recordToolBarCheckBox[4] = false;
            CheckBox5.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[4] = true;
            CheckBox5.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox5ActionPerformed

    private void CheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox6ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[5]) {
            recordToolBarCheckBox[5] = false;
            CheckBox6.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[5] = true;
            CheckBox6.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox6ActionPerformed

    private void CheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox7ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[6]) {
            recordToolBarCheckBox[6] = false;
            CheckBox7.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[6] = true;
            CheckBox7.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox7ActionPerformed

    private void CheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox8ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[7]) {
            recordToolBarCheckBox[7] = false;
            CheckBox8.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[7] = true;
            CheckBox8.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox8ActionPerformed

    private void CheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox9ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[8]) {
            recordToolBarCheckBox[8] = false;
            CheckBox9.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[8] = true;
            CheckBox9.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox9ActionPerformed

    private void CheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBox10ActionPerformed
        // TODO add your handling code here:
        if (recordToolBarCheckBox[9]) {
            recordToolBarCheckBox[9] = false;
            CheckBox10.setIcon(new ImageIcon(checkFalse01));
        } else {
            recordToolBarCheckBox[9] = true;
            CheckBox10.setIcon(new ImageIcon(checkTrue01));
        }
    }//GEN-LAST:event_CheckBox10ActionPerformed

    private void btn_switchStepBeatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_switchStepBeatActionPerformed
        // TODO add your handling code here:
        if (switchStepBeat) {
            switchStepBeat = false;
            btn_switchStepBeat.setIcon(new ImageIcon(imgStepBeat01));
        } else {
            switchStepBeat = true;
            btn_switchStepBeat.setIcon(new ImageIcon(imgStepBeat02));
        }
    }//GEN-LAST:event_btn_switchStepBeatActionPerformed

    private void btn_switchBarMinutesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_switchBarMinutesActionPerformed
        // TODO add your handling code here:
        if (switchBarMinutes) {
            switchBarMinutes = false;
            btn_switchBarMinutes.setIcon(new ImageIcon(imgBarMinutes01));
        } else {
            switchBarMinutes = true;
            btn_switchBarMinutes.setIcon(new ImageIcon(imgBarMinutes02));
        }
    }//GEN-LAST:event_btn_switchBarMinutesActionPerformed

    private void btn_viewPlayMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPlayMousePressed
        // TODO add your handling code here:
        btn_viewPlay.setIcon(new ImageIcon(imgViewPlay02));
    }//GEN-LAST:event_btn_viewPlayMousePressed

    private void btn_viewPlayMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPlayMouseReleased
        // TODO add your handling code here:
        btn_viewPlay.setIcon(new ImageIcon(imgViewPlay01));
    }//GEN-LAST:event_btn_viewPlayMouseReleased

    private void btn_viewStepMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewStepMousePressed
        // TODO add your handling code here:
        btn_viewStep.setIcon(new ImageIcon(imgViewStep02));
    }//GEN-LAST:event_btn_viewStepMousePressed

    private void btn_viewStepMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewStepMouseReleased
        // TODO add your handling code here:
        btn_viewStep.setIcon(new ImageIcon(imgViewStep01));
    }//GEN-LAST:event_btn_viewStepMouseReleased

    private void btn_viewPianoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPianoMousePressed
        // TODO add your handling code here:
        btn_viewPiano.setIcon(new ImageIcon(imgViewPiano02));
    }//GEN-LAST:event_btn_viewPianoMousePressed

    private void btn_viewPianoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPianoMouseReleased
        // TODO add your handling code here:
        btn_viewPiano.setIcon(new ImageIcon(imgViewPiano01));
    }//GEN-LAST:event_btn_viewPianoMouseReleased

    private void btn_viewBrowserMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewBrowserMousePressed
        // TODO add your handling code here:
        btn_viewBrowser.setIcon(new ImageIcon(imgViewBrowser02));
    }//GEN-LAST:event_btn_viewBrowserMousePressed

    private void btn_viewBrowserMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewBrowserMouseReleased
        // TODO add your handling code here:
        btn_viewBrowser.setIcon(new ImageIcon(imgViewBrowser01));
    }//GEN-LAST:event_btn_viewBrowserMouseReleased

    private void btn_viewMixerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMixerMousePressed
        // TODO add your handling code here:
        btn_viewMixer.setIcon(new ImageIcon(imgViewMixer02));
    }//GEN-LAST:event_btn_viewMixerMousePressed

    private void btn_viewMixerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMixerMouseReleased
        // TODO add your handling code here:
        btn_viewMixer.setIcon(new ImageIcon(imgViewMixer01));
    }//GEN-LAST:event_btn_viewMixerMouseReleased

    private void btn_undoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_undoMousePressed
        // TODO add your handling code here:
        btn_undo.setIcon(new ImageIcon(imgUndo02));
    }//GEN-LAST:event_btn_undoMousePressed

    private void btn_undoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_undoMouseReleased
        // TODO add your handling code here:
        btn_undo.setIcon(new ImageIcon(imgUndo01));
    }//GEN-LAST:event_btn_undoMouseReleased

    private void btn_saveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMousePressed
        // TODO add your handling code here:
        btn_save.setIcon(new ImageIcon(imgSave02));
    }//GEN-LAST:event_btn_saveMousePressed

    private void btn_saveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseReleased
        // TODO add your handling code here:
        btn_save.setIcon(new ImageIcon(imgSave01));
    }//GEN-LAST:event_btn_saveMouseReleased

    private void btn_renderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_renderMousePressed
        // TODO add your handling code here:
        btn_render.setIcon(new ImageIcon(imgRender02));
    }//GEN-LAST:event_btn_renderMousePressed

    private void btn_renderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_renderMouseReleased
        // TODO add your handling code here:
        btn_render.setIcon(new ImageIcon(imgRender01));
    }//GEN-LAST:event_btn_renderMouseReleased

    private void btn_audioEditorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_audioEditorMousePressed
        // TODO add your handling code here:
        btn_audioEditor.setIcon(new ImageIcon(imgAudioEditor02));
    }//GEN-LAST:event_btn_audioEditorMousePressed

    private void btn_audioEditorMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_audioEditorMouseReleased
        // TODO add your handling code here:
        btn_audioEditor.setIcon(new ImageIcon(imgAudioEditor01));
    }//GEN-LAST:event_btn_audioEditorMouseReleased

    private void btn_recordingMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_recordingMousePressed
        // TODO add your handling code here:
        btn_recording.setIcon(new ImageIcon(imgRecording02));
    }//GEN-LAST:event_btn_recordingMousePressed

    private void btn_recordingMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_recordingMouseReleased
        // TODO add your handling code here:
        btn_recording.setIcon(new ImageIcon(imgRecording01));
    }//GEN-LAST:event_btn_recordingMouseReleased

    private void btn_infoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_infoMousePressed
        // TODO add your handling code here:
        btn_info.setIcon(new ImageIcon(imgInfo02));
    }//GEN-LAST:event_btn_infoMousePressed

    private void btn_infoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_infoMouseReleased
        // TODO add your handling code here:
        btn_info.setIcon(new ImageIcon(imgInfo01));
    }//GEN-LAST:event_btn_infoMouseReleased

    private void btn_aboutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_aboutMousePressed
        // TODO add your handling code here:
        btn_about.setIcon(new ImageIcon(imgAbout02));
    }//GEN-LAST:event_btn_aboutMousePressed

    private void btn_aboutMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_aboutMouseReleased
        // TODO add your handling code here:
        btn_about.setIcon(new ImageIcon(imgAbout01));
    }//GEN-LAST:event_btn_aboutMouseReleased

    private void btn_minMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_minMousePressed
        // TODO add your handling code here:
        btn_min.setIcon(new ImageIcon(imgMin02));
    }//GEN-LAST:event_btn_minMousePressed

    private void btn_minMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_minMouseReleased
        // TODO add your handling code here:
        btn_min.setIcon(new ImageIcon(imgMin01));
    }//GEN-LAST:event_btn_minMouseReleased

    private void btn_maxMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_maxMousePressed
        // TODO add your handling code here:
        btn_max.setIcon(new ImageIcon(imgMax02));
    }//GEN-LAST:event_btn_maxMousePressed

    private void btn_maxMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_maxMouseReleased
        // TODO add your handling code here:
        btn_max.setIcon(new ImageIcon(imgMax01));
    }//GEN-LAST:event_btn_maxMouseReleased

    private void btn_closeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMousePressed
        // TODO add your handling code here:
        btn_close.setIcon(new ImageIcon(imgClose02));
    }//GEN-LAST:event_btn_closeMousePressed

    private void btn_closeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseReleased
        // TODO add your handling code here:
        btn_close.setIcon(new ImageIcon(imgClose01));
    }//GEN-LAST:event_btn_closeMouseReleased

    private void btn_syncAMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncAMousePressed
        // TODO add your handling code here:
        btn_syncA.setIcon(new ImageIcon(imgSyncA02));
    }//GEN-LAST:event_btn_syncAMousePressed

    private void btn_syncAMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncAMouseReleased
        // TODO add your handling code here:
        btn_syncA.setIcon(new ImageIcon(imgSyncA01));
    }//GEN-LAST:event_btn_syncAMouseReleased

    private void btn_syncBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncBMousePressed
        // TODO add your handling code here:
        btn_syncB.setIcon(new ImageIcon(imgSyncB02));
    }//GEN-LAST:event_btn_syncBMousePressed

    private void btn_syncBMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncBMouseReleased
        // TODO add your handling code here:
        btn_syncB.setIcon(new ImageIcon(imgSyncB01));
    }//GEN-LAST:event_btn_syncBMouseReleased

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        // TODO add your handling code here:
        exit(0);
    }//GEN-LAST:event_btn_closeActionPerformed

    private void btn_fileMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_fileMousePressed
        // TODO add your handling code here:
        btn_file.setIcon(new ImageIcon(imgFile02));
    }//GEN-LAST:event_btn_fileMousePressed

    private void btn_fileMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_fileMouseReleased
        // TODO add your handling code here:
        btn_file.setIcon(new ImageIcon(imgFile01));
    }//GEN-LAST:event_btn_fileMouseReleased

    private void btn_editMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editMousePressed
        // TODO add your handling code here:
        btn_edit.setIcon(new ImageIcon(imgEdit02));
    }//GEN-LAST:event_btn_editMousePressed

    private void btn_editMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editMouseReleased
        // TODO add your handling code here:
        btn_edit.setIcon(new ImageIcon(imgEdit01));
    }//GEN-LAST:event_btn_editMouseReleased

    private void btn_channelsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_channelsMousePressed
        // TODO add your handling code here:
        btn_channels.setIcon(new ImageIcon(imgChannels02));
    }//GEN-LAST:event_btn_channelsMousePressed

    private void btn_channelsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_channelsMouseReleased
        // TODO add your handling code here:
        btn_channels.setIcon(new ImageIcon(imgChannels01));
    }//GEN-LAST:event_btn_channelsMouseReleased

    private void btn_viewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMousePressed
        // TODO add your handling code here:
        btn_view.setIcon(new ImageIcon(imgView02));
    }//GEN-LAST:event_btn_viewMousePressed

    private void btn_viewMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMouseReleased
        // TODO add your handling code here:
        btn_view.setIcon(new ImageIcon(imgView01));
    }//GEN-LAST:event_btn_viewMouseReleased

    private void btn_optionsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_optionsMousePressed
        // TODO add your handling code here:
        btn_options.setIcon(new ImageIcon(imgOptions02));
    }//GEN-LAST:event_btn_optionsMousePressed

    private void btn_optionsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_optionsMouseReleased
        // TODO add your handling code here:
        btn_options.setIcon(new ImageIcon(imgOptions01));
    }//GEN-LAST:event_btn_optionsMouseReleased

    private void btn_toolsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_toolsMousePressed
        // TODO add your handling code here:
        btn_tools.setIcon(new ImageIcon(imgTools02));
    }//GEN-LAST:event_btn_toolsMousePressed

    private void btn_toolsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_toolsMouseReleased
        // TODO add your handling code here:
        btn_tools.setIcon(new ImageIcon(imgTools01));
    }//GEN-LAST:event_btn_toolsMouseReleased

    private void btn_helpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_helpMousePressed
        // TODO add your handling code here:
        btn_help.setIcon(new ImageIcon(imgHelp02));
    }//GEN-LAST:event_btn_helpMousePressed

    private void btn_helpMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_helpMouseReleased
        // TODO add your handling code here:
        btn_help.setIcon(new ImageIcon(imgHelp01));
    }//GEN-LAST:event_btn_helpMouseReleased

    private void btn_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_fileActionPerformed
        // TODO add your handling code here:
        if(menuClicked) {
            menuClicked = false;
            PopupFile.show(this, btn_file.getX() + 5, btn_file.getY() + 25);
        } else {
            menuClicked = true;
            PopupFile.show(this, btn_file.getX() + 5, btn_file.getY() + 25);
        }
    }//GEN-LAST:event_btn_fileActionPerformed

    private void newItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemActionPerformed
        // TODO add your handling code here:
            TimeArrow = new TimeArrow();
            InstrumentBar.addMouseListener(this);
            projectPanel.addMouseListener(this);
            
            try {
                TimeArrow.setIcon();
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeBar.add(TimeArrow, 2);
            TimeArrow.setLocation(-7, 0);
            
            timer = new Timer();
            UpdateOffset = new UpdateOffset();
        try {
            UpdateOffset.setTempoDigits(TempoDigits3, TempoDigits2, TempoDigits1);
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            timer.setUsage(RAMDigit4, RAMDigit3, RAMDigit2, RAMDigit1, MemMeterFill);
            timer.setTimeArrow(TimeArrow);
            timer.setTimerDigits(TimeDigits6, TimeDigits5, TimeDigits4, TimeDigits3, TimeDigits2, TimeDigits1);
            timer.setStatusLabel(statusLabel);
            timer.setScrollPane(trackScrollPane, timeScrollPane, projectScrollPane);
            
            tm = new Thread(timer);

            projectScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
            projectScrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
            
            int barChunkCount = 400;
            BarPanel = new BarPanel[barChunkCount];
            for (int i = 0; i < barChunkCount; i++) {
                BarPanel[i] = new BarPanel();
                
                if (i % 4 == 0) {
                    BarPanel[i].setBoldBorder();
                } else {
                    BarPanel[i].setNormalBorder();
                }
                
                if ((i % 32) < 16) {
                    BarPanel[i].setNormalBackground();
                } else {
                    BarPanel[i].setBoldBackground();
                }
                
                BarPanel[i].setXPos((BarPanel[i].w * i ) + 1);
                BarPanel[i].setLocation((BarPanel[i].w * i) + 1, 0);
                projectPanel.add(BarPanel[i], 0, -1);
            }
            
            timer.setBarPanel(BarPanel);
            TrackPanel = new TrackPanel[1];
            barRowLine = new BarRowLine[200];
            for (int i = 0; i < 200; i++) {
                barRowLine[i] = new BarRowLine();
                barRowLine[i].setLocation(0, ((TrackPanel[0].HEIGHT + 1) * i - 1));
                projectPanel.add(barRowLine[i], 2, -1);
            }
            timer.setBarRowLine(barRowLine);
            
            projectStart = true;
            jPanel1.setVisible(true);
            
             TimeBarNumber = new TimeBarNumber[150];
             BarPanel = new BarPanel[1];
        
        for (int i = 0; i < 150; i++) {
            TimeBarNumber[i] = new TimeBarNumber();
            
            TimeBarNumber[i].setLabelFont(i + 1);
            TimeBarNumber[i].setText("" + (i + 1));
            TimeBarNumber[i].setLocation(i * BarPanel[0].w * 4, 0);
            TimeBarNumber[i].setSize(30, 30);
            timeBar.add(TimeBarNumber[i]);
        }
        
        tm.start();

    }//GEN-LAST:event_newItemActionPerformed

    private void openItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openItemActionPerformed
        // TODO add your handling code here:
        int returnVal = jFileChooser1.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            fileNameLabel.setText(file.getName());
            TimeArrow = new TimeArrow();
            
            try {
                TimeArrow.setIcon();
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeBar.add(TimeArrow, 2);
            TimeArrow.setLocation(-7, 0);
            
            timer = new Timer();
            UpdateOffset = new UpdateOffset();
            try {
                UpdateOffset.setTempoDigits(TempoDigits3, TempoDigits2, TempoDigits1);
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            timer.setUsage(RAMDigit4, RAMDigit3, RAMDigit2, RAMDigit1, MemMeterFill);
            timer.setTimeArrow(TimeArrow);
            timer.setTimerDigits(TimeDigits6, TimeDigits5, TimeDigits4, TimeDigits3, TimeDigits2, TimeDigits1);
            timer.setStatusLabel(statusLabel);
            timer.setScrollPane(trackScrollPane, timeScrollPane, projectScrollPane);
            
            tm = new Thread(timer);
            
            projectScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
            projectScrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
            
            if(file == null)
                return;
 		
            try {
                openFile = new MidiFile(file.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AWTException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        //  deltaToMillisec(openFile.getTracks(), openFile.getTicks());
            TrackPanel = new TrackPanel[openFile.getTrackCounts()];
            DrawPanel = new DrawPanel[openFile.getTrackCounts()];
            Tracks = openFile.getTracks();
            ticks = openFile.getTicks();
            
            for (int i = 0; i < openFile.getTrackCounts(); i++) {
                int r = (int)((Math.random() * 10000) % 8); 
                TrackPanel[i] = new TrackPanel(r);
                VTrackPanel.add(TrackPanel[i]);

                try {
                    TrackPanel[i].addMute();
                } catch (IOException ex) {
                    Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                TrackPanel[i].setTrackNumber(i + 1);
               
                for (int j = 0; j < Tracks.get(i).size(); j++) {
                    if ((Tracks.get(i).get(j).eventType != MidiFile.MetaEvent) && (Tracks.get(i).get(j).eventType != MidiFile.SysexEvent1) &&
                        (Tracks.get(i).get(j).eventType != MidiFile.SysexEvent2)) {
                        TrackPanel[i].setChannel(Tracks.get(i).get(j).channel);
                        break;
                    }
                }
                
                for (int j = 0; j < Tracks.get(i).size(); j++) {
                    if (Tracks.get(i).get(j).eventType == MidiFile.EventProgramChange) {
                        TrackPanel[i].setInstrument(Tracks.get(i).get(j).instrument);
                        break;
                    }
                }
                
                TrackPanel[i].setSize(96, TrackPanel[i].HEIGHT);     // 56
                TrackPanel[i].setLocation(0, (TrackPanel[i].HEIGHT + 1) * i);        //56
                TrackPanel[i].setTrackName(" FM             ");
                TrackPanel[i].addMouseListener(this);
                InstrumentBar.add(TrackPanel[i]);
            }
            
            drawPanelFunc(openFile.getTracks(), openFile.getTicks());
            
            int barChunkCount = 400;
            BarPanel = new BarPanel[barChunkCount];
            for (int i = 0; i < barChunkCount; i++) {
                BarPanel[i] = new BarPanel();
                
                if (i % 4 == 0) {
                    BarPanel[i].setBoldBorder();
                } else {
                    BarPanel[i].setNormalBorder();
                }
                
                if ((i % 32) < 16) {
                    BarPanel[i].setNormalBackground();
                } else {
                    BarPanel[i].setBoldBackground();
                }
                
                BarPanel[i].setXPos((BarPanel[i].w * i ) + 1);
                BarPanel[i].setLocation((BarPanel[i].w * i) + 1, 0);
                projectPanel.add(BarPanel[i], 0, -1);
            }
            
            timer.setBarPanel(BarPanel);
            
            barRowLine = new BarRowLine[200];
            for (int i = 0; i < 200; i++) {
                barRowLine[i] = new BarRowLine();
                barRowLine[i].setLocation(0, ((TrackPanel[0].HEIGHT + 1) * i - 1));
                projectPanel.add(barRowLine[i], 2, -1);
            }
            timer.setBarRowLine(barRowLine);
            
            for (int i = 0; i < openFile.getTrackCounts(); i++) {
                projectPanel.add(DrawPanel[i], 4, -1);
            }
            timer.setDrawPanel(DrawPanel);
            
            projectStart = true;
            jPanel1.setVisible(true);
            
            tm.start();
        }
    }//GEN-LAST:event_openItemActionPerformed

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        // TODO add your handling code here:
        exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    private void btn_minActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_minActionPerformed
        // TODO add your handling code here:
        this.setState(this.ICONIFIED);
    }//GEN-LAST:event_btn_minActionPerformed

    private void btn_playbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_playbackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_playbackActionPerformed

    private void btn_playListOptionsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playListOptionsMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Playlist options");
    }//GEN-LAST:event_btn_playListOptionsMouseEntered

    private void btn_snapToGridMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_snapToGridMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Snap to grid");
    }//GEN-LAST:event_btn_snapToGridMouseEntered

    private void btn_drawMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_drawMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Draw");
    }//GEN-LAST:event_btn_drawMouseEntered

    private void btn_paintMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paintMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Paint");
    }//GEN-LAST:event_btn_paintMouseEntered

    private void btn_deleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Delete");
    }//GEN-LAST:event_btn_deleteMouseEntered

    private void btn_muteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_muteMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Mute");
    }//GEN-LAST:event_btn_muteMouseEntered

    private void btn_slipMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_slipMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Slip");
    }//GEN-LAST:event_btn_slipMouseEntered

    private void btn_sliceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_sliceMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Slice");
    }//GEN-LAST:event_btn_sliceMouseEntered

    private void btn_selectMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Select");
    }//GEN-LAST:event_btn_selectMouseEntered

    private void btn_zoomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zoomMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Zoom");
    }//GEN-LAST:event_btn_zoomMouseEntered

    private void btn_playbackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playbackMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_playbackMouseClicked

    private void btn_playbackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playbackMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Playback");
    }//GEN-LAST:event_btn_playbackMouseEntered

    private void btn_EEPlayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EEPlayMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Play / pause song");
    }//GEN-LAST:event_btn_EEPlayMouseEntered

    private void btn_closeStructureMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeStructureMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Close");
    }//GEN-LAST:event_btn_closeStructureMouseEntered

    private void btn_browserOptionsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_browserOptionsMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Browser options");
    }//GEN-LAST:event_btn_browserOptionsMouseEntered

    private void btn_collapseStructureMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_collapseStructureMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Collapse structure");
    }//GEN-LAST:event_btn_collapseStructureMouseEntered

    private void btn_refreshStructureMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshStructureMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Reread structure");
    }//GEN-LAST:event_btn_refreshStructureMouseEntered

    private void btn_playListOptionsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playListOptionsMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_playListOptionsMouseExited

    private void btn_minMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_minMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Minimize");
    }//GEN-LAST:event_btn_minMouseEntered

    private void btn_minMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_minMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_minMouseExited

    private void btn_maxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_maxMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Maximize / restore");
    }//GEN-LAST:event_btn_maxMouseEntered

    private void btn_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Close");
    }//GEN-LAST:event_btn_closeMouseEntered

    private void btn_syncBMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncBMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("MIDI / audio sync beat");
    }//GEN-LAST:event_btn_syncBMouseEntered

    private void btn_syncAMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncAMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("MIDI input activity");
    }//GEN-LAST:event_btn_syncAMouseEntered

    private void btn_switchStepBeatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_switchStepBeatMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Switch step / beat display");
    }//GEN-LAST:event_btn_switchStepBeatMouseEntered

    private void btn_switchBarMinutesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_switchBarMinutesMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Switch bar / minute display");
    }//GEN-LAST:event_btn_switchBarMinutesMouseEntered

    private void btn_psMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_psMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Pattern / song mode");
    }//GEN-LAST:event_btn_psMouseEntered

    private void resetButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Reset");
    }//GEN-LAST:event_resetButtonMouseEntered

    private void playButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Play");
    }//GEN-LAST:event_playButtonMouseEntered

    private void stopButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopButtonMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Stop");
    }//GEN-LAST:event_stopButtonMouseEntered

    private void CheckBox1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox1MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Typing keyboard to piano keyboard");
    }//GEN-LAST:event_CheckBox1MouseEntered

    private void CheckBox2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox2MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Countdown before recording");
    }//GEN-LAST:event_CheckBox2MouseEntered

    private void CheckBox3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox3MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Blend recording (overdub)");
    }//GEN-LAST:event_CheckBox3MouseEntered

    private void CheckBox4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox4MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Loop recording");
    }//GEN-LAST:event_CheckBox4MouseEntered

    private void CheckBox5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox5MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Enable note / clip groups");
    }//GEN-LAST:event_CheckBox5MouseEntered

    private void CheckBox6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox6MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Metronome");
    }//GEN-LAST:event_CheckBox6MouseEntered

    private void CheckBox7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox7MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Wait for input to start playing");
    }//GEN-LAST:event_CheckBox7MouseEntered

    private void CheckBox8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox8MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Step editing mode");
    }//GEN-LAST:event_CheckBox8MouseEntered

    private void CheckBox9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox9MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Scrolls to reach time markers");
    }//GEN-LAST:event_CheckBox9MouseEntered

    private void CheckBox10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox10MouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Multilink to controllers");
    }//GEN-LAST:event_CheckBox10MouseEntered

    private void btn_viewPlayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPlayMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View playlist");
    }//GEN-LAST:event_btn_viewPlayMouseEntered

    private void btn_viewStepMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewStepMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View step sequencer");
    }//GEN-LAST:event_btn_viewStepMouseEntered

    private void btn_viewPianoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPianoMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View piano roll");
    }//GEN-LAST:event_btn_viewPianoMouseEntered

    private void btn_viewBrowserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewBrowserMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View browser / plugin picker");
    }//GEN-LAST:event_btn_viewBrowserMouseEntered

    private void btn_viewMixerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMixerMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View mixer");
    }//GEN-LAST:event_btn_viewMixerMouseEntered

    private void btn_undoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_undoMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Undo / undo history");
    }//GEN-LAST:event_btn_undoMouseEntered

    private void btn_saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Save as... / save new version");
    }//GEN-LAST:event_btn_saveMouseEntered

    private void btn_renderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_renderMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Render as audio file...");
    }//GEN-LAST:event_btn_renderMouseEntered

    private void btn_audioEditorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_audioEditorMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Open (new) audio editor");
    }//GEN-LAST:event_btn_audioEditorMouseEntered

    private void btn_recordingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_recordingMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("One-click audio recording");
    }//GEN-LAST:event_btn_recordingMouseEntered

    private void btn_infoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_infoMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("View project info");
    }//GEN-LAST:event_btn_infoMouseEntered

    private void btn_aboutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_aboutMouseEntered
        // TODO add your handling code here:
        statusLabel.setText("Help / about");
    }//GEN-LAST:event_btn_aboutMouseEntered

    private void btn_snapToGridMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_snapToGridMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_snapToGridMouseExited

    private void btn_drawMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_drawMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_drawMouseExited

    private void btn_paintMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paintMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_paintMouseExited

    private void btn_deleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_deleteMouseExited

    private void btn_muteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_muteMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_muteMouseExited

    private void btn_slipMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_slipMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_slipMouseExited

    private void btn_sliceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_sliceMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_sliceMouseExited

    private void btn_selectMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_selectMouseExited

    private void btn_zoomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zoomMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_zoomMouseExited

    private void btn_playbackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playbackMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_playbackMouseExited

    private void btn_EEPlayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EEPlayMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_EEPlayMouseExited

    private void btn_browserOptionsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_browserOptionsMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_browserOptionsMouseExited

    private void btn_collapseStructureMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_collapseStructureMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_collapseStructureMouseExited

    private void btn_refreshStructureMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshStructureMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_refreshStructureMouseExited

    private void btn_closeStructureMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeStructureMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_closeStructureMouseExited

    private void btn_maxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_maxMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_maxMouseExited

    private void btn_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_closeMouseExited

    private void btn_syncBMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncBMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_syncBMouseExited

    private void btn_syncAMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_syncAMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_syncAMouseExited

    private void btn_psMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_psMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_psMouseExited

    private void btn_switchStepBeatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_switchStepBeatMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_switchStepBeatMouseExited

    private void btn_switchBarMinutesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_switchBarMinutesMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_switchBarMinutesMouseExited

    private void resetButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_resetButtonMouseExited

    private void playButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_playButtonMouseExited

    private void stopButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopButtonMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_stopButtonMouseExited

    private void CheckBox1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox1MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox1MouseExited

    private void CheckBox2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox2MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox2MouseExited

    private void CheckBox3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox3MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox3MouseExited

    private void CheckBox4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox4MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox4MouseExited

    private void CheckBox5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox5MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox5MouseExited

    private void CheckBox6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox6MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox6MouseExited

    private void CheckBox7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox7MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox7MouseExited

    private void CheckBox8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox8MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox8MouseExited

    private void CheckBox9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox9MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox9MouseExited

    private void CheckBox10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBox10MouseClicked

    private void CheckBox10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CheckBox10MouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_CheckBox10MouseExited

    private void btn_viewPlayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPlayMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_viewPlayMouseExited

    private void btn_viewStepMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewStepMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_viewStepMouseExited

    private void btn_viewPianoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewPianoMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_viewPianoMouseExited

    private void btn_viewBrowserMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewBrowserMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_viewBrowserMouseExited

    private void btn_viewMixerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_viewMixerMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_viewMixerMouseExited

    private void btn_undoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_undoMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_undoMouseExited

    private void btn_saveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_saveMouseExited

    private void btn_renderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_renderMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_renderMouseExited

    private void btn_audioEditorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_audioEditorMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_audioEditorMouseExited

    private void btn_recordingMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_recordingMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_recordingMouseExited

    private void btn_infoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_infoMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_infoMouseExited

    private void btn_aboutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_aboutMouseExited
        // TODO add your handling code here:
        statusLabel.setText("");
    }//GEN-LAST:event_btn_aboutMouseExited

    private void keyboardLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_keyboardLabelMousePressed
        // TODO add your handling code here:
        VstiPanel = new VstiPanel();
        try {
            VstiPanel.setIcon();
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Panel Created!");
    }//GEN-LAST:event_keyboardLabelMousePressed

    private void addInstItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInstItemActionPerformed
        // TODO add your handling code here:
        int r = (int)((Math.random() * 10000) % 8); 
        TrackPanel TrackPanel = new TrackPanel(r);
        VstiTrack vstiTrack = new VstiTrack();
        ArrayList<MidiEvent> event = new ArrayList<MidiEvent>();
        ArrayList<PianoNote> note = new ArrayList<PianoNote>();
        Track.add(event);
        Notes.add(note);
        VTrackPanel.add(TrackPanel);
        VVstiTrack.add(vstiTrack);

        try {
            VTrackPanel.get(VTrackPanelCount).addMute();
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }

        VTrackPanel.get(VTrackPanelCount).setTrackNumber(trackCounts + 1);
        VTrackPanel.get(VTrackPanelCount).setChannel(trackCounts);
        VTrackPanel.get(VTrackPanelCount).setInstrument(0);

        VTrackPanel.get(VTrackPanelCount).setSize(96, TrackPanel.HEIGHT);     // 56
        VTrackPanel.get(VTrackPanelCount).setLocation(0, (TrackPanel.HEIGHT + 1) * VTrackPanelCount);        //56
        VTrackPanel.get(VTrackPanelCount).setTrackName(" FM             ");
        VTrackPanel.get(VTrackPanelCount).addMouseListener(this);
        InstrumentBar.add(VTrackPanel.get(VTrackPanelCount));
        VTrackPanelCount++;
        trackCounts++;
    }//GEN-LAST:event_addInstItemActionPerformed

    private void btn_drawMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_drawMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.DRAW;
        btn_draw.setIcon(new ImageIcon(imgDraw02));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_drawMousePressed

    private void btn_paintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_paintMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.PAINT;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint02));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_paintMousePressed

    private void btn_deleteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.DELETE;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete02));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_deleteMousePressed

    private void btn_muteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_muteMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.MUTE;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute02));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_muteMousePressed

    private void btn_slipMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_slipMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.SLIP;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip02));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_slipMousePressed

    private void btn_sliceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_sliceMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.SLICE;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice02));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_sliceMousePressed

    private void btn_selectMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_selectMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.SELECT;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect02));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_selectMousePressed

    private void btn_zoomMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_zoomMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.ZOOM;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom02));
        btn_playback.setIcon(new ImageIcon(imgPlayback01));
    }//GEN-LAST:event_btn_zoomMousePressed

    private void btn_playbackMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_playbackMousePressed
        // TODO add your handling code here:
        TOOL = TOOL.PLAYBACK;
        btn_draw.setIcon(new ImageIcon(imgDraw01));
        btn_paint.setIcon(new ImageIcon(imgPaint01));
        btn_delete.setIcon(new ImageIcon(imgDelete01));
        btn_mute.setIcon(new ImageIcon(imgMute01));
        btn_slip.setIcon(new ImageIcon(imgSlip01));
        btn_slice.setIcon(new ImageIcon(imgSlice01));
        btn_select.setIcon(new ImageIcon(imgSelect01));
        btn_zoom.setIcon(new ImageIcon(imgZoom01));
        btn_playback.setIcon(new ImageIcon(imgPlayback02));
    }//GEN-LAST:event_btn_playbackMousePressed

    private void projectPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectPanelMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_projectPanelMouseMoved

    private void projectPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectPanelMouseDragged
        // TODO add your handling code here:
        if (drawMousePressed) {
            VDrawPanel.get(memIdx).setSize( 32 * ( (evt.getX() - memX) / 32 ), TrackPanel[0].HEIGHT);
        }
    }//GEN-LAST:event_projectPanelMouseDragged

    private void btn_viewPianoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_viewPianoActionPerformed
        try {
            // TODO add your handling code here:
            pianoRoll.setIcon();
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
        pianoRollOn();
    }//GEN-LAST:event_btn_viewPianoActionPerformed

    private void TransportToolBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TransportToolBarMousePressed
        // TODO add your handling code here:
        isTempoPressed = true;
        memTempoY = evt.getY();
    }//GEN-LAST:event_TransportToolBarMousePressed

    private void TransportToolBarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TransportToolBarMouseReleased
        // TODO add your handling code here:
        isTempoPressed = false;
    }//GEN-LAST:event_TransportToolBarMouseReleased

    private void TransportToolBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TransportToolBarMouseDragged
        // TODO add your handling code here:
        if (isTempoPressed == true) {
            if (evt.getY() < memTempoY) {
                UpdateOffset.setTempo(tempo + 1);
                tempo++;
            } else if (evt.getY() > memTempoY) {
                UpdateOffset.setTempo(tempo - 1);
                tempo--;
            }
        }
    }//GEN-LAST:event_TransportToolBarMouseDragged
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Graphic.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Graphic.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Graphic.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graphic.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Graphic().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CPUDigit1;
    private javax.swing.JLabel CPUDigit2;
    private javax.swing.JLabel CPUMeter;
    private javax.swing.JLabel CPUPolyDigit1;
    private javax.swing.JLabel CPUPolyDigit2;
    private javax.swing.JLabel CPUPolyDigit3;
    private javax.swing.JLabel CPUPolyDigit4;
    private javax.swing.JLabel CPUToolBar;
    private javax.swing.JButton CheckBox1;
    private javax.swing.JButton CheckBox10;
    private javax.swing.JButton CheckBox2;
    private javax.swing.JButton CheckBox3;
    private javax.swing.JButton CheckBox4;
    private javax.swing.JButton CheckBox5;
    private javax.swing.JButton CheckBox6;
    private javax.swing.JButton CheckBox7;
    private javax.swing.JButton CheckBox8;
    private javax.swing.JButton CheckBox9;
    private javax.swing.JLabel ControlToolBar;
    private javax.swing.JLabel Copyright;
    private javax.swing.JMenu ImportMenu;
    private javax.swing.JPanel InstrumentBar;
    private javax.swing.JLabel MemMeter;
    private javax.swing.JLabel MemMeterFill;
    private javax.swing.JLabel MonitorToolBar;
    private javax.swing.JLabel OnlineToolBar;
    private javax.swing.JPanel PLToolbarPanel;
    private javax.swing.JLabel PatternDigits1;
    private javax.swing.JPopupMenu PopupFile;
    private javax.swing.JPopupMenu PopupTrack;
    private javax.swing.JLabel RAMDigit1;
    private javax.swing.JLabel RAMDigit2;
    private javax.swing.JLabel RAMDigit3;
    private javax.swing.JLabel RAMDigit4;
    private javax.swing.JLabel RecordToolBar;
    private javax.swing.JLabel ShortcutToolBar;
    private javax.swing.JLabel ShortcutToolBar2;
    private javax.swing.JLabel TempoDigits1;
    private javax.swing.JLabel TempoDigits2;
    private javax.swing.JLabel TempoDigits3;
    private javax.swing.JLabel TimeDigits1;
    private javax.swing.JLabel TimeDigits2;
    private javax.swing.JLabel TimeDigits3;
    private javax.swing.JLabel TimeDigits4;
    private javax.swing.JLabel TimeDigits5;
    private javax.swing.JLabel TimeDigits6;
    private javax.swing.JLabel TimeToolBar;
    private javax.swing.JLabel TransportToolBar;
    private javax.swing.JMenuItem addAudioItem;
    private javax.swing.JMenuItem addInstItem;
    private javax.swing.JMenuItem addSampleOne;
    private javax.swing.JLabel background;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JLabel border_left;
    private javax.swing.JLabel border_top;
    private javax.swing.JButton btn_EEPlay;
    private javax.swing.JButton btn_about;
    private javax.swing.JButton btn_audioEditor;
    private javax.swing.JButton btn_browserOptions;
    private javax.swing.JButton btn_channels;
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_closeStructure;
    private javax.swing.JButton btn_collapseStructure;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_draw;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_file;
    private javax.swing.JButton btn_help;
    private javax.swing.JButton btn_info;
    private javax.swing.JButton btn_max;
    private javax.swing.JButton btn_min;
    private javax.swing.JButton btn_mute;
    private javax.swing.JButton btn_options;
    private javax.swing.JButton btn_paint;
    private javax.swing.JButton btn_playListOptions;
    private javax.swing.JButton btn_playback;
    private javax.swing.JButton btn_ps;
    private javax.swing.JButton btn_recording;
    private javax.swing.JButton btn_refreshStructure;
    private javax.swing.JButton btn_render;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_select;
    private javax.swing.JButton btn_slice;
    private javax.swing.JButton btn_slip;
    private javax.swing.JButton btn_snapToGrid;
    private javax.swing.JButton btn_switchBarMinutes;
    private javax.swing.JButton btn_switchStepBeat;
    private javax.swing.JButton btn_syncA;
    private javax.swing.JButton btn_syncB;
    private javax.swing.JButton btn_tools;
    private javax.swing.JButton btn_undo;
    private javax.swing.JButton btn_view;
    private javax.swing.JButton btn_viewBrowser;
    private javax.swing.JButton btn_viewMixer;
    private javax.swing.JButton btn_viewPiano;
    private javax.swing.JButton btn_viewPlay;
    private javax.swing.JButton btn_viewStep;
    private javax.swing.JButton btn_zoom;
    private javax.swing.JLabel channelIconLabel;
    private javax.swing.JLabel channelLabel;
    private javax.swing.JLabel eventListIcon;
    private javax.swing.JLabel eventListLabel;
    private javax.swing.JPanel eventListPanel;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenuItem exportMIDIItem;
    private javax.swing.JMenuItem exportMP3Item;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenuItem exportWaveItem;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JMenuItem importAudioItem;
    private javax.swing.JMenuItem importMIDIItem;
    private javax.swing.JLabel insertsIcon;
    private javax.swing.JLabel insertsLabel;
    private javax.swing.JPanel insertsPanel;
    private javax.swing.JLabel instIconLabel;
    private javax.swing.JLabel instLabel;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JLabel keyboardIcon;
    private javax.swing.JLabel keyboardLabel;
    private javax.swing.JPanel keyboardPanel;
    private javax.swing.JLabel midiInputIconLabel;
    private javax.swing.JLabel midiInputLabel;
    private javax.swing.JMenuItem newItem;
    private javax.swing.JMenuItem openItem;
    private javax.swing.JLabel panEmptyLabel;
    private javax.swing.JLabel panFillLabel;
    private javax.swing.JLabel panIconLabel;
    private javax.swing.JLabel panValueLabel;
    private javax.swing.JLabel panelBorder1;
    private javax.swing.JLabel panelBorder2;
    private javax.swing.JLabel panelBorder3;
    private javax.swing.JLabel panelBorder4;
    private javax.swing.JLabel panelBorder5;
    private javax.swing.JLabel pianoRollIcon;
    private javax.swing.JLabel pianoRollLabel;
    private javax.swing.JPanel pianoRollPanel;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel playListLabel;
    private javax.swing.JLayeredPane projectPanel;
    private javax.swing.JScrollPane projectScrollPane;
    private javax.swing.JButton resetButton;
    private javax.swing.JMenuItem saveAsItem;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JLabel sendsIcon;
    private javax.swing.JLabel sendsLabel;
    private javax.swing.JPanel sendsPanel;
    private javax.swing.JPanel statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopButton;
    private javax.swing.JLayeredPane timeBar;
    private javax.swing.JLabel timeNumLabel;
    private javax.swing.JScrollPane timeScrollPane;
    private javax.swing.JPanel trackInfoBar;
    private javax.swing.JPanel trackLeftBorder;
    private javax.swing.JLabel trackMixerIcon;
    private javax.swing.JLabel trackNameLabel;
    private javax.swing.JPanel trackNameMarker;
    private javax.swing.JPanel trackNamePanel;
    private javax.swing.JLabel trackNumberLabel;
    private javax.swing.JLabel trackRightArrow;
    private javax.swing.JPanel trackRightBorder;
    private javax.swing.JScrollPane trackScrollPane;
    private javax.swing.JLabel trackWindow;
    private javax.swing.JPanel trackWindowPanel;
    private javax.swing.JLabel volumeEmptyLabel;
    private javax.swing.JLabel volumeFillLabel;
    private javax.swing.JLabel volumeIconLabel;
    private javax.swing.JLabel volumeValueLabel;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/icon.png")));
        
        try {
            InputStream fontStream = getClass().getResourceAsStream("fonts/Fruity microfont.ttf");
            Font microFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            microFont = microFont.deriveFont(Font.PLAIN, 14);
            fontStream.close();
            
            timeNumLabel.setFont(microFont);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        try {
            checkFalse01 = ImageIO.read(getClass().getResource("resources/CheckBox_False01.png"));
            checkTrue01 = ImageIO.read(getClass().getResource("resources/CheckBox_True01.png"));
            imgStepBeat01 = ImageIO.read(getClass().getResource("resources/TimeBtn01.png"));
            imgStepBeat02 = ImageIO.read(getClass().getResource("resources/TimeBtn02.png"));
            imgBarMinutes01 = ImageIO.read(getClass().getResource("resources/TimeBtn03.png"));
            imgBarMinutes02 = ImageIO.read(getClass().getResource("resources/TimeBtn04.png"));
            imgViewPlay01 = ImageIO.read(getClass().getResource("resources/btn_viewPlay.png"));
            imgViewPlay02 = ImageIO.read(getClass().getResource("resources/btn_viewPlay2.png"));
            imgViewStep01 = ImageIO.read(getClass().getResource("resources/btn_viewStep.png"));
            imgViewStep02 = ImageIO.read(getClass().getResource("resources/btn_viewStep2.png"));
            imgViewPiano01 = ImageIO.read(getClass().getResource("resources/btn_viewPiano.png"));
            imgViewPiano02 = ImageIO.read(getClass().getResource("resources/btn_viewPiano2.png"));
            imgViewBrowser01 = ImageIO.read(getClass().getResource("resources/btn_viewBrowser.png"));
            imgViewBrowser02 = ImageIO.read(getClass().getResource("resources/btn_viewBrowser2.png"));
            imgViewMixer01 = ImageIO.read(getClass().getResource("resources/btn_viewMixer.png"));
            imgViewMixer02 = ImageIO.read(getClass().getResource("resources/btn_viewMixer2.png"));
            imgUndo01 = ImageIO.read(getClass().getResource("resources/btn_undo.png"));
            imgUndo02 = ImageIO.read(getClass().getResource("resources/btn_undo2.png"));
            imgSave01 = ImageIO.read(getClass().getResource("resources/btn_save.png"));
            imgSave02 = ImageIO.read(getClass().getResource("resources/btn_save2.png"));
            imgRender01 = ImageIO.read(getClass().getResource("resources/btn_render.png"));
            imgRender02 = ImageIO.read(getClass().getResource("resources/btn_render2.png"));
            imgAudioEditor01 = ImageIO.read(getClass().getResource("resources/btn_audioEditor.png"));
            imgAudioEditor02 = ImageIO.read(getClass().getResource("resources/btn_audioEditor2.png"));
            imgRecording01 = ImageIO.read(getClass().getResource("resources/btn_recording.png"));
            imgRecording02 = ImageIO.read(getClass().getResource("resources/btn_recording2.png"));
            imgInfo01 = ImageIO.read(getClass().getResource("resources/btn_info.png"));
            imgInfo02 = ImageIO.read(getClass().getResource("resources/btn_info2.png"));
            imgAbout01 = ImageIO.read(getClass().getResource("resources/btn_about.png"));
            imgAbout02 = ImageIO.read(getClass().getResource("resources/btn_about2.png"));
            imgMin01 = ImageIO.read(getClass().getResource("resources/btn_min.png"));
            imgMin02 = ImageIO.read(getClass().getResource("resources/btn_min2.png"));
            imgMax01 = ImageIO.read(getClass().getResource("resources/btn_max.png"));
            imgMax02 = ImageIO.read(getClass().getResource("resources/btn_max2.png"));
            imgClose01 = ImageIO.read(getClass().getResource("resources/btn_close.png"));
            imgClose02 = ImageIO.read(getClass().getResource("resources/btn_close2.png"));
            imgSyncA01 = ImageIO.read(getClass().getResource("resources/btn_syncA_01.png"));
            imgSyncA02 = ImageIO.read(getClass().getResource("resources/btn_syncA_02.png"));
            imgSyncB01 = ImageIO.read(getClass().getResource("resources/btn_syncB_01.png"));
            imgSyncB02 = ImageIO.read(getClass().getResource("resources/btn_syncB_02.png"));
            imgFile01 = ImageIO.read(getClass().getResource("resources/btn_menuFile.png"));
            imgFile02 = ImageIO.read(getClass().getResource("resources/btn_menuFile2.png"));
            imgEdit01 = ImageIO.read(getClass().getResource("resources/btn_menuEdit.png"));
            imgEdit02 = ImageIO.read(getClass().getResource("resources/btn_menuEdit2.png"));
            imgChannels01 = ImageIO.read(getClass().getResource("resources/btn_menuChannels.png"));
            imgChannels02 = ImageIO.read(getClass().getResource("resources/btn_menuChannels2.png"));
            imgView01 = ImageIO.read(getClass().getResource("resources/btn_menuView.png"));
            imgView02 = ImageIO.read(getClass().getResource("resources/btn_menuView2.png"));
            imgOptions01 = ImageIO.read(getClass().getResource("resources/btn_menuOptions.png"));
            imgOptions02 = ImageIO.read(getClass().getResource("resources/btn_menuOptions2.png"));
            imgTools01 = ImageIO.read(getClass().getResource("resources/btn_menuTools.png"));
            imgTools02 = ImageIO.read(getClass().getResource("resources/btn_menuTools2.png"));
            imgHelp01 = ImageIO.read(getClass().getResource("resources/btn_menuHelp.png"));
            imgHelp02 = ImageIO.read(getClass().getResource("resources/btn_menuHelp2.png"));
            imgDraw01 = ImageIO.read(getClass().getResource("resources/btn_draw.png"));
            imgDraw02 = ImageIO.read(getClass().getResource("resources/btn_draw2.png"));
            imgPaint01 = ImageIO.read(getClass().getResource("resources/btn_paint.png"));
            imgPaint02 = ImageIO.read(getClass().getResource("resources/btn_paint2.png"));
            imgDelete01 = ImageIO.read(getClass().getResource("resources/btn_delete.png"));
            imgDelete02 = ImageIO.read(getClass().getResource("resources/btn_delete2.png"));
            imgMute01 = ImageIO.read(getClass().getResource("resources/btn_mute.png"));
            imgMute02 = ImageIO.read(getClass().getResource("resources/btn_mute2.png"));
            imgSlip01 = ImageIO.read(getClass().getResource("resources/btn_slip.png"));
            imgSlip02 = ImageIO.read(getClass().getResource("resources/btn_slip2.png"));
            imgSlice01 = ImageIO.read(getClass().getResource("resources/btn_slice.png"));
            imgSlice02 = ImageIO.read(getClass().getResource("resources/btn_slice2.png"));
            imgSelect01 = ImageIO.read(getClass().getResource("resources/btn_select.png"));
            imgSelect02 = ImageIO.read(getClass().getResource("resources/btn_select2.png"));
            imgZoom01 = ImageIO.read(getClass().getResource("resources/btn_zoom.png"));
            imgZoom02 = ImageIO.read(getClass().getResource("resources/btn_zoom2.png"));
            imgPlayback01 = ImageIO.read(getClass().getResource("resources/btn_playback.png"));
            imgPlayback02 = ImageIO.read(getClass().getResource("resources/btn_playback2.png"));
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deltaToMillisec(ArrayList<ArrayList<MidiEvent>> Tracks, int ticks) {
        float deltaTimeInSeconds;
        int t;

        System.out.println("All size : " + Tracks.size());
       // for (int i = 0; i < Tracks.size(); i++) {
        //    System.out.println(i + " : " + Tracks.get(i).size());
       // }
        
        for (int i = 0; i < Tracks.size(); i++) {
            for (int j = 0; j < Tracks.get(i).size(); j++) {
                t = Tracks.get(i).get(j).deltaTime;
                if (t > 0) {
                    deltaTimeInSeconds = t * (1000.0f * (60.0f / ((60000000 / 67) * ticks))); //t * (t / (ticks * 1000));
                    System.out.println("Current Track Num : " + i + "\n" + j + "th Note delta time : " + deltaTimeInSeconds);
                }
               
             //System.out.println(Tracks.get(i).get(j).deltaTime);
            }
        }
    }

    public void drawPanelFunc(ArrayList<ArrayList<MidiEvent>> Tracks, int ticks) {
        BarPanel BarPanel = new BarPanel();
        int starttime = 0;
        int endtime = 0;
        int x1 = 0;
        int x2 = 0;
        
        for (int i = 0; i < openFile.getTrackCounts(); i++) {
            DrawPanel[i] = new DrawPanel();
            VstiTrack vstiTrack = new VstiTrack();
            ArrayList<PianoNote> note = new ArrayList<PianoNote>();
            Notes.add(note);
            VVstiTrack.add(vstiTrack);
            
            Track.add(Tracks.get(i));
            pianoRollScreen.setTicks(ticks);
            pianoRollScreen.setPianoRollTrack(Tracks.get(i), note);
            pianoRollScreen.loadNotes();
            
            DrawPanel[i].addMouseListener(this);
            VTrackPanelCount++;
            trackCounts++;
            VDrawPanel.add(DrawPanel[i]);
            double tmp;
          
            for (int j = 0; j < Tracks.get(i).size(); j++) {
                if (Tracks.get(i).get(j).eventType == MidiFile.EventNoteOn && Tracks.get(i).get(j).velocity > 0) {
                    starttime = Tracks.get(i).get(j).startTime;
                    tmp = (double)starttime / ticks * BarPanel.w;
                    x1 = (int)tmp;
                    break;
                }
            }
            
            for (int j = Tracks.get(i).size() - 1; j > 0; j--) {
                if (Tracks.get(i).get(j).eventType == MidiFile.EventNoteOn && Tracks.get(i).get(j).velocity > 0) {
                    endtime = Tracks.get(i).get(j).startTime + Tracks.get(i).get(j).deltaTime;
                    tmp = (double)endtime / ticks * BarPanel.w;
                    x2 = (int)tmp + 150;
                    System.out.println("X2 is : " + x2);
                    break;
                }
            }
            DrawPanel[i].setSize(x2 - x1, TrackPanel[i].HEIGHT + 1);
            DrawPanel[i].setLocation(x1, (TrackPanel[i].HEIGHT + 1) * i);
            DrawPanel[i].setColor(TrackPanel[i].getColor());
            DrawPanel[i].setBackgroundColor();
            
            DrawPanel[i].drawNotePreview(Tracks.get(i), ticks);
        }
        
        TimeBarNumber = new TimeBarNumber[150];
        
        for (int i = 0; i < 150; i++) {
            TimeBarNumber[i] = new TimeBarNumber();
            
            TimeBarNumber[i].setLabelFont(i + 1);
            TimeBarNumber[i].setText("" + (i + 1));
            TimeBarNumber[i].setLocation(i * BarPanel.w * 4, 0);
            TimeBarNumber[i].setSize(30, 30);
            timeBar.add(TimeBarNumber[i]);
        }
    }
    
    public void pianoRollOn() {
        if (!isPianoRoll) {
            isPianoRoll = true;
            pianoRollScreen.setPianoRollTrack(Track.get(selectIdx), Notes.get(selectIdx));
            System.out.println("pianoRollOn idx : " + selectIdx);
            System.out.println("Track size : " + Track.size());
            System.out.println("Note size : " + Notes.size());
            pianoRollScreen.setVstiTrack(VVstiTrack.get(selectIdx));
            pianoRollScreen.showNotes();
            UpdateOffset.setIsPlaying(true);
          
            InstrumentBar.setVisible(false);
            projectPanel.setVisible(false);
            trackScrollPane.remove(InstrumentBar);
            projectScrollPane.remove(projectPanel);
            trackScrollPane.add(pianoRoll);
            projectScrollPane.add(pianoRollScreen);

            pianoRoll.setVisible(true);
            pianoRoll.setBounds(0, 0, 69, 2812);
            pianoRoll.setSize(69, 2812);
            pianoRoll.setPreferredSize(new Dimension(69, 2812));

            pianoRollScreen.setVisible(true);
            pianoRollScreen.setBounds(0, 0, 24000, 2812);
            pianoRollScreen.setSize(24000, 2812);
            pianoRollScreen.setPreferredSize(new Dimension(24000, 2812));

            trackScrollPane.setSize(69, 917);
            trackScrollPane.setPreferredSize(new Dimension(69, 917));
            trackScrollPane.setViewportView(pianoRoll);
            projectScrollPane.setViewportView(pianoRollScreen);
            pianoRoll.reset();
            projectScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(true));
            projectScrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI(true));

            for (int i = 0; i < 150; i++) {
                TimeBarNumber[i].setLocation(i * PRBarPanel.w * 4 * 4, 0);
            }
        } else {
            isPianoRoll = false;
            
            pianoRoll.setVisible(false);
            pianoRollScreen.setVisible(false);
            pianoRollScreen.hideNotes();
            trackScrollPane.remove(pianoRoll);
            projectScrollPane.remove(pianoRollScreen);
            trackScrollPane.add(InstrumentBar);
            projectScrollPane.add(projectPanel);
            UpdateOffset.setIsPlaying(false);

            InstrumentBar.setVisible(true);
            InstrumentBar.setBounds(0, 0, 600, 1600);
            InstrumentBar.setSize(600, 1600);
            InstrumentBar.setPreferredSize(new Dimension(600, 1600));
            
         //   VDrawPanel.get(selectIdx).redraw();
            VDrawPanel.get(selectIdx).drawNotePreview(Track.get(selectIdx), ticks);
            projectPanel.setVisible(true);
            projectPanel.setBounds(0, 0, 12000, 1600);
            projectPanel.setSize(12000, 1600);
            projectPanel.setPreferredSize(new Dimension(12000, 1600));

            trackScrollPane.setSize(102, 917);
            trackScrollPane.setPreferredSize(new Dimension(102, 917));
            trackScrollPane.setViewportView(InstrumentBar);
            projectScrollPane.setViewportView(projectPanel);
            projectScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(false));
            projectScrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI(false));
            
            BarPanel tmp = new BarPanel();
            for (int i = 0; i < 150; i++) {
                TimeBarNumber[i].setLocation(i * tmp.w * 4, 0);
            }
            pianoRoll.reset();
        }
    }
}