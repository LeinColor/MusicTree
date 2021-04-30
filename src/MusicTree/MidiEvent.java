package MusicTree;

/**
 * MidiEvent의 정보들이 담기는 객체이다.
 * MidiEvent의 집합은 곧 Track이 된다. ArrayList에 이 정보들이 담긴다.
 * 
 * 값들을 변경하는 일이 워낙 잦고,
 * Getter, Setter를 만들어봤었는데 코드의 가독성이 매우 떨어졌기에 public으로 구현하였다.
 * 
 * @author 12151430 이건영
 */
public class MidiEvent {
	public int eventType;           // 이벤트 타입을 담는다. (NoteOn, NoteOff, Pitch, ProgramChange 등)
	public int startTime;           // 노트의 시작 시간
	public int deltaTime;           // 노트의 델타 타임
	public int metaEvent;           // 템포 또는 박자가 변경됐을 경우 변경된다.
	public int metaLength;          // 메타 이벤트 마커의 뒤에서 나오는 16진수 값의 길이를 표시한다.
	public byte[] metaValue;        // 메타 이벤트 마커 뒷부분의 16진수 값을 하나씩 받아온다.
	public boolean eventFlag;
	
	public int channel;             // 채널
	public int noteNumber;          // 계이름
	public int velocity;            // 음의 세기
	public int instrument;          // 악기의 종류
	public int keyPressure;         // 노트를 누르는 세기
	public int channelPressure;     // 채널 변경
	public int controlNum;          // 컨트롤의 종류
	public int controlValue;        // 컨트롤의 종류에 해당되는 값
	public int pitchBend;           // 음의 피치(높이)
	
	public int tempo;               // 템포
	public int numerator;           // 박자의 분모에 해당하는 값
	public int denominator;         // 박자의 분자에 해당하는 값
}