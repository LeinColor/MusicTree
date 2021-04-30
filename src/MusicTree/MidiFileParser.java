package MusicTree;

import java.io.*;

/**
 * Midi 파일을 읽는 데 필요한 클래스이다.
 * 각 바이트 크기만큼 파일을 읽어오는 메소드들이 있으며, 현재 읽고 있는 오프셋을 저장한다.
 * 
 * @author 12151430 이건영
 */
public class MidiFileParser {
    private byte[] data;
    private int offset;
        
    // 생성자에서 파일을 불러오며, 디버깅용으로 니블을 추출해 헥스값을 출력한다.
    public MidiFileParser(String filename) throws IOException, FileNotFoundException {
        File file = new File(filename);

        if(!file.exists()) {
                System.err.println(filename + " is not exists.");
        }
        if(file.length() == 0) {
                System.err.println(filename + " is empty file.");
        }

        int len = 0;
        data = new byte[(int)file.length()];
        FileInputStream byteReader = new FileInputStream(file);

        len = byteReader.read(data);
        System.out.println("File Size : " + len);
        for(int i =0; i < len; i++) {
                String[] hexTable = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
                int highNibble = (data[i] >> 4) & 0xF;
                int lowNibble = data[i] & 0xF;

                System.out.print(hexTable[highNibble]);
                System.out.print(hexTable[lowNibble] + " ");
        }

        offset = 0;
        System.out.println(len);
    }
	
    // 현재 오프셋을 가져온다.
    public int GetOffset() {
        return offset;
    }

    // 현재 오프셋에 담긴 데이터를 확인한다. 단, 오프셋은 변화시키지 않는다.
    public byte Pop() {
        return data[offset];
    }

    // 1바이트를 읽고 오프셋을 +1 시킨다.
    public byte ReadByte() {
        byte x = data[offset];
        offset++;
        return x;
    }

    // 지정한 바이트만큼 읽고 오프셋을 지정량만큼 증가시킨다.
    public byte[] ReadBytes(int amount) {
        byte[] chunk = new byte[amount];
        for (int i = 0; i < amount; i++) {
                chunk[i] = data[i + offset];
        }

        offset += amount;
        return chunk;
    }

    // 아스키코드로 읽은 문자를 문자열로 변환시키고 오프셋을 지정량만큼 증가시킨다.
    public String ReadAscii(int len) {
        String s;
        char[] temp = new char[len];

        for(int i = 0; i < len; i++) {
                temp[i] = (char)data[i + offset]; 
        }
        offset += len;

        s = new String(temp, 0, temp.length);
        return s;
    }
	
    // 2바이트를 읽고 오프셋을 +2 시킨다.
    public int ReadShort() {
        int x = ( (int)((data[offset] & 0xff) << 8) | (int)(data[offset+1] & 0xff) );
        offset += 2;
        return x;
    }

    // 4바이트를 읽고 오프셋을 +4 시킨다.
    public int ReadInt() {
        int x = ( (int)((data[offset] & 0xff) << 24) | (int)((data[offset+1] & 0xff) << 16) |
                  (int)((data[offset+2] & 0xff) << 8) | (int)(data[offset+3] & 0xff) );
        offset += 4;
        return x;
    }
	
    /**
    * Variable-Length Quantities는 7비트 표기법이다.
    * 바이트를 읽고 tmp에 저장시킨다.
    
    * 만약 tmp가 0x7F(10진수로 127)보다 크면 바이트를 또 읽고 tmp에 저장한다.
    * 그 후 이전에 읽었던 tmp값의 비트를 왼쪽으로 7칸 이동시킨다.
    * 마지막으로 방금 읽었던 tmp값을 0x7F와 and 연산하여 더한다.
    * 
    * Delta-Time과 Byte-Length는 이 표기법을 쓰는데,
    * 값이 충분히 큰 데이터도 나올 수 있으므로 이 루틴 실행 회수를 넉넉히 3회로 잡는다.
    */
    public int ReadVarlen() {
        int result = 0;
        byte tmp;

        tmp = ReadByte();
        result = (int)(tmp & 0x7F);

        for (int i = 0; i < 3; i++) {
            if ((tmp & 0x80) != 0) {
                tmp = ReadByte();
                result = (int)((result << 7) + (tmp & 0x7F));           
            } else {
                break;
            }
        }
        return (int)result;
    }
}
