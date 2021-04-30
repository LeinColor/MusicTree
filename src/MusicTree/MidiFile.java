package MusicTree;

import java.awt.AWTException;
import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * FileRead에서 MIDI 파일의 Header Chunk를 읽는다.
 * TrackRead에서 MIDI 파일의 Track Chunk를 읽는다.
 * 
 * ticksPerQuarterNotes는 재생속도와 노트의 시작 지점을 결정하는 데 중요한 정보이다.
 * trackCounts는 트랙의 갯수를 나타낸다.
 * 
 * Events는 MidiEvent들을 담는 ArrayList이다.
 * Tracks는 Events들을 담는 2D Array of ArrayList이다.
 * 
 * @author 12151430 이건영
 */
public class MidiFile {
	public boolean hasFlag = false;
	private int ticksPerQuarterNotes = 0;
        private int trackCounts = 0;
	private ArrayList<MidiEvent> Events;
	private ArrayList<ArrayList<MidiEvent>> Tracks;
	private Player SoundPlayer;
       
	public static final int EventNoteOff = 0x80;
	public static final int EventNoteOn = 0x90;
	public static final int EventKeyPressure = 0xA0;
	public static final int EventControlChange = 0xB0;
	public static final int EventProgramChange = 0xC0;
	public static final int EventChannelPressure = 0xD0;
	public static final int EventPitchBend = 0xE0;
	public static final int SysexEvent1 = 0xF0;
	public static final int SysexEvent2 = 0xF7;
	public static final int MetaEvent = 0xFF;
	public static final int MetaEventSequence = 0x0;
	public static final int MetaEventText = 0x1;
	public static final int MetaEventCopyright = 0x2;
	public static final int MetaEventSequenceName = 0x3;
	public static final int MetaEventInstrument = 0x4;
	public static final int MetaEventLyric = 0x5;
	public static final int MetaEventMarker = 0x6;
	public static final int MetaEventEndOfTrack = 0x2F;
	public static final int MetaEventTempo = 0x51;
	public static final int MetaEventSMPTEOffset = 0x54;
	public static final int MetaEventTimeSignature = 0x58;
	public static final int MetaEventKeySignature = 0x59;
        
    public static String[] Instruments = {

        "Acoustic Grand Piano",
        "Bright Acoustic Piano",
        "Electric Grand Piano",
        "Honky-tonk Piano",
        "Electric Piano 1",
        "Electric Piano 2",
        "Harpsichord",
        "Clavi",
        "Celesta",
        "Glockenspiel",
        "Music Box",
        "Vibraphone",
        "Marimba",
        "Xylophone",
        "Tubular Bells",
        "Dulcimer",
        "Drawbar Organ",
        "Percussive Organ",
        "Rock Organ",
        "Church Organ",
        "Reed Organ",
        "Accordion",
        "Harmonica",
        "Tango Accordion",
        "Acoustic Guitar (nylon)",
        "Acoustic Guitar (steel)",
        "Electric Guitar (jazz)",
        "Electric Guitar (clean)",
        "Electric Guitar (muted)",
        "Overdriven Guitar",
        "Distortion Guitar",
        "Guitar harmonics",
        "Acoustic Bass",
        "Electric Bass (finger)",
        "Electric Bass (pick)",
        "Fretless Bass",
        "Slap Bass 1",
        "Slap Bass 2",
        "Synth Bass 1",
        "Synth Bass 2",
        "Violin",
        "Viola",
        "Cello",
        "Contrabass",
        "Tremolo Strings",
        "Pizzicato Strings",
        "Orchestral Harp",
        "Timpani",
        "String Ensemble 1",
        "String Ensemble 2",
        "SynthStrings 1",
        "SynthStrings 2",
        "Choir Aahs",
        "Voice Oohs",
        "Synth Voice",
        "Orchestra Hit",
        "Trumpet",
        "Trombone",
        "Tuba",
        "Muted Trumpet",
        "French Horn",
        "Brass Section",
        "SynthBrass 1",
        "SynthBrass 2",
        "Soprano Sax",
        "Alto Sax",
        "Tenor Sax",
        "Baritone Sax",
        "Oboe",
        "English Horn",
        "Bassoon",
        "Clarinet",
        "Piccolo",
        "Flute",
        "Recorder",
        "Pan Flute",
        "Blown Bottle",
        "Shakuhachi",
        "Whistle",
        "Ocarina",
        "Lead 1 (square)",
        "Lead 2 (sawtooth)",
        "Lead 3 (calliope)",
        "Lead 4 (chiff)",
        "Lead 5 (charang)",
        "Lead 6 (voice)",
        "Lead 7 (fifths)",
        "Lead 8 (bass + lead)",
        "Pad 1 (new age)",
        "Pad 2 (warm)",
        "Pad 3 (polysynth)",
        "Pad 4 (choir)",
        "Pad 5 (bowed)",
        "Pad 6 (metallic)",
        "Pad 7 (halo)",
        "Pad 8 (sweep)",
        "FX 1 (rain)",
        "FX 2 (soundtrack)",
        "FX 3 (crystal)",
        "FX 4 (atmosphere)",
        "FX 5 (brightness)",
        "FX 6 (goblins)",
        "FX 7 (echoes)",
        "FX 8 (sci-fi)",
        "Sitar",
        "Banjo",
        "Shamisen",
        "Koto",
        "Kalimba",
        "Bag pipe",
        "Fiddle",
        "Shanai",
        "Tinkle Bell",
        "Agogo",
        "Steel Drums",
        "Woodblock",
        "Taiko Drum",
        "Melodic Tom",
        "Synth Drum",
        "Reverse Cymbal",
        "Guitar Fret Noise",
        "Breath Noise",
        "Seashore",
        "Bird Tweet",
        "Telephone Ring",
        "Helicopter",
        "Applause",
        "Gunshot",
        "GM Drum"
    };

	public MidiFile(String filename) throws FileNotFoundException, IOException, AWTException, LineUnavailableException, UnsupportedAudioFileException {
		MidiFileParser file = new MidiFileParser(filename);
		FileRead(file, filename);
		hasFlag = true;
	}

	public void FileRead(MidiFileParser file, String filename) throws AWTException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		String header = file.ReadAscii(4);
		System.out.println("");
		if (header.equals("MThd"))
			System.out.println("MThd 체크 완료");

		long goodbit = file.ReadInt();
		if (goodbit == 6)
			System.out.println("6 값 확인 완료");

		int format = file.ReadShort();
		if (format >= 0 && format <= 2)
			System.out.println("MIDI Type : " + format);

		trackCounts = file.ReadShort();
		System.out.println("Track Counts : " + trackCounts);

		ticksPerQuarterNotes = (int) file.ReadShort() & 0xFFFF;
		System.out.println("Ticks per Quarter Notes : " + (ticksPerQuarterNotes));

		int currentTrackNumber = 0;
		
		Tracks = new ArrayList<ArrayList<MidiEvent>>();
		Events = new ArrayList<MidiEvent>(trackCounts);
		while (currentTrackNumber < trackCounts) {
			String trackChunk = file.ReadAscii(4);
			if (trackChunk.equals("MTrk")) {
				System.out.println("MTrk 확인 완료!");
				currentTrackNumber++;
				Events = TrackRead(file);
				Tracks.add(Events);
				System.out.println(currentTrackNumber);
			}
		}
                
                
	//	SoundPlayer = new Player();
	//	SoundPlayer.loadSound(Tracks, ticksPerQuarterNotes);
        //        SoundPlayer.playSound();
        //        SoundPlayer.start();
	}
	
	public void newSoundPlayer() {
	//	SoundPlayer = new Player();
	}
	
	public void stopSoundPlayer() {
	//	SoundPlayer.stopSound();
	}
        
        public void pauseSoundPlayer() {
        //    SoundPlayer.pauseSound();
        }
        
        public void playSoundPlayer() {
        //    SoundPlayer.playSound();
        }
	
	public int getMilliseconds() {
		return SoundPlayer.getMilliseconds();
	}
        
        public ArrayList<ArrayList<MidiEvent>> getTracks() {
            return Tracks;
        }
        
        public int getTicks() {
            return ticksPerQuarterNotes;
        }
        
	public ArrayList<MidiEvent> TrackRead(MidiFileParser file) {
		int startTime = 0;
		int deltaTime = 0;
		int drawEvent = 0;
		int eventType = 0;
		int trackLen = file.ReadInt();
		int trackEnd = trackLen + file.GetOffset();
		ArrayList<MidiEvent> trackData = new ArrayList<MidiEvent>();
		
		System.out.println("Track length : " + trackLen);

		while (file.GetOffset() < trackEnd) {
			MidiEvent event = new MidiEvent();
			trackData.add(event);
			
			deltaTime = file.ReadVarlen();
			startTime += deltaTime;
			event.startTime = startTime;
			event.deltaTime = deltaTime;

			drawEvent = (int) file.Pop() & 0xff;
			if (drawEvent >= EventNoteOff) {
				event.eventFlag = true;
				eventType = (int) file.ReadByte() & 0xff;
			}

			if (eventType >= EventNoteOff && eventType < EventNoteOff + 16) {
				event.eventType = EventNoteOff;
				event.channel = eventType & 0xf;
				event.noteNumber = ((int) file.ReadByte() & 0xff) - 36;
				event.velocity = ((int) file.ReadByte() & 0xff);
			} else if (eventType >= EventNoteOn && eventType < EventNoteOn + 16) {
				event.eventType = EventNoteOn;
				event.channel = eventType & 0xf;
				event.noteNumber = ((int) file.ReadByte() & 0xff) - 36;
				event.velocity = ((int) file.ReadByte() & 0xff);
			} else if (eventType >= EventKeyPressure && eventType < EventKeyPressure + 16) {
				event.eventType = EventKeyPressure;
				event.channel = eventType & 0xf;
				event.noteNumber = ((int) file.ReadByte() & 0xff);
				event.velocity = ((int) file.ReadByte() & 0xff);
			} else if (eventType >= EventControlChange && eventType < EventControlChange + 16) {
				event.eventType = EventControlChange;
				event.channel = eventType & 0xf;
				event.controlNum = ((int) file.ReadByte() & 0xff);
				event.controlValue = ((int) file.ReadByte() & 0xff);
			} else if (eventType >= EventProgramChange && eventType < EventProgramChange + 16) {
				event.eventType = EventProgramChange;
				event.channel = eventType & 0xf;
				event.instrument = ((int) file.ReadByte() & 0xff);
			} else if (eventType >= EventChannelPressure && eventType < EventChannelPressure + 16) {
				event.eventType = EventChannelPressure;
				event.channel = eventType & 0xf;
				event.channelPressure = ((int) file.ReadByte() & 0xff);
			} else if (eventType == SysexEvent1) {
				event.eventType = SysexEvent1;
				event.metaLength = file.ReadVarlen();
				event.metaValue = (file.ReadBytes(event.metaLength));
			} else if (eventType == SysexEvent2) {
				event.eventType = SysexEvent2;
				event.metaLength = file.ReadVarlen();
				event.metaValue = file.ReadBytes(event.metaLength);
			} else if (eventType == MetaEvent) {
				event.eventType = MetaEvent;
				event.metaEvent = ((int) file.ReadByte() & 0xff);
				event.metaLength = file.ReadVarlen();
				event.metaValue = file.ReadBytes(event.metaLength);
				if (event.metaEvent == MetaEventTimeSignature) {
					if (event.metaLength != 4) {
						System.out.println("박자에 잘못된 값이 들어감");
					} else {
						event.denominator = event.metaValue[0];
						event.numerator = ((int) Math.pow(2, event.metaValue[1]));
					}
				} else if (event.metaEvent == MetaEventKeySignature) {
					System.out.println("Key signature check successed!");
				} else if (event.metaEvent == MetaEventTempo) {
					if (event.metaLength != 3) {
						System.out.println("템포데이터가 이상함");
					} else {
						event.tempo = 60000000 / ((int) ((event.metaValue[0] & 0xff) << 16) | (int) ((event.metaValue[1] & 0xff) << 8) | (int) (event.metaValue[2] & 0xff));
					}
				} else if (event.metaEvent == MetaEventEndOfTrack) {
					System.out.println("<Track End>");
					//break;
				}
			}
			//System.out.println("Delta time : " + deltaTime);
			//System.out.println("Total Time : " + startTime);
			System.out.println("Event type : " + eventType);
			//System.out.println(event.getChannel() + " " + event.getNoteNumber() + " " + event.getVelocity());
			//System.out.println(event.getMetaEvent() + " " + event.getMetaLength() + " " + event.getDenominator() + " " + event.getNumerator());
		}
		return trackData;
	}
        
        public int getTrackCounts() {
            return trackCounts;
        }
        
}
