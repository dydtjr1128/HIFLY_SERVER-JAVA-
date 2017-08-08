import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Spliterator;
import java.util.Vector;

public class CombineVideo {
	

	private byte[] buffer;
	String s;
	List list;

	File dataFile = null;
	File makeFile = null;
	File dummyFile = null;
	
	FileInputStream dataFIS = null;
	DataInputStream dataDIS = null;
	
	FileOutputStream makeFOS = null;
	DataOutputStream makeDOS = null;
	
	FileInputStream dummyFIS = null;
	DataInputStream dummyDIS = null;
	byte[] dummyBuffer;
	byte[] pattern = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x09, (byte) 0x10 };
	public CombineVideo() {
		try {
			buffer = new byte[7825720];

			dataFile = new File("C://Users//CYSN//Desktop//자바//datafile");
			makeFile = new File("C://Users//CYSN//Desktop//자바//makefile.mp4");
			dummyFile = new File("C://Users//CYSN//Desktop//자바//dummy.h264");
			dummyBuffer = new byte[(int) dummyFile.length()];

			dummyFIS = new FileInputStream(dummyFile);			
			dummyDIS = new DataInputStream(dummyFIS);
			makeFOS = new FileOutputStream(makeFile);
			makeDOS = new DataOutputStream(makeFOS);
			
			dataFIS = new FileInputStream(dataFile);
			dataDIS = new DataInputStream(dataFIS);
			dataDIS.read(buffer);		
			list = split(pattern, buffer);
			
			dummyDIS.read(dummyBuffer);
			makeDOS.write(dummyBuffer);
			for (int i = 0; i < list.size(); i++) {
				makeDOS.write(pattern);
				makeDOS.write((byte[]) list.get(i));				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dummyFIS.close();
				dummyDIS.close();
				dataFIS.close();
				dataDIS.close();
				makeDOS.close();
				makeFOS.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isMatch(byte[] pattern, byte[] input, int pos) {
		for (int i = 0; i < pattern.length; i++) {
			if (pattern[i] != input[pos + i]) {
				return false;
			}
		}
		return true;
	}

	public List<byte[]> split(byte[] pattern, byte[] input) {
		List<byte[]> l = new LinkedList<byte[]>();
		int blockStart = 0;
		for (int i = 0; i < input.length; i++) {
			if (isMatch(pattern, input, i)) {
				l.add(Arrays.copyOfRange(input, blockStart, i));
				blockStart = i + pattern.length;
				i = blockStart;
			}
		}
		l.add(Arrays.copyOfRange(input, blockStart, input.length));
		return l;
	}
	public static void main(String[] args) {
		new CombineVideo();
	}
}