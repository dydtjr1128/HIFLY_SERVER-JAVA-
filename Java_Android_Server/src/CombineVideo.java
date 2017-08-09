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

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class CombineVideo extends Thread{
	

	//private byte[] buffer;
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
	byte[] pattern = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x09, (byte) 0x10 };//IFrame
	byte[] data1 = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x67};//SPS
	byte[] data2 = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x68};//PPS
	byte[] data3 = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x65};//IDR
	byte[] sps = {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x67, (byte)0x42, (byte)0x00, (byte)0x0a, (byte)0xf8, (byte)0x41, (byte)0xa2};
	byte[] pps = {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x68, (byte)0xce, (byte)0x38, (byte)0x80};
	int count=0;
	public CombineVideo() {	}
	public void recordStart(String fileName){//준
		try {
		//	buffer = new byte[7825720];

			//dataFile = new File("C://Users//CYSN//Desktop//자바//datafile");
			makeFile = new File("C://Users//CYSN//Desktop//자바//" + fileName + ".h264");
			dummyFile = new File("C://Users//CYSN//Desktop//자바//dummy.h264");
			dummyBuffer = new byte[(int) dummyFile.length()];

			dummyFIS = new FileInputStream(dummyFile);			
			dummyDIS = new DataInputStream(dummyFIS);
			makeFOS = new FileOutputStream(makeFile);
			makeDOS = new DataOutputStream(makeFOS);
			
			/*
			dataFIS = new FileInputStream(dataFile);
			dataDIS = new DataInputStream(dataFIS);
			dataDIS.read(buffer);		
			list = split(pattern, buffer);
			
			dummyDIS.read(dummyBuffer);
			makeDOS.write(dummyBuffer);
			for (int i = 0; i < list.size(); i++) {
				makeDOS.write(pattern);
				makeDOS.write((byte[]) list.get(i));				
			}*/
			dummyDIS.read(dummyBuffer);
			makeDOS.write(dummyBuffer);
		} catch (Exception e) {
			recordEnd();
			e.printStackTrace();
		}
		NativeLibrary.addSearchPath("libvlc", "C:\\Users\\CYSN\\Desktop\\자바\\vlcj");
		//NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "the-directory");

	     String media = makeFile.getAbsolutePath(); // example = file:///C:/test.mp4
	     // you are gonna use below value on the client 
	     String[] options = {":sout=#rtp{sdp=rtsp://localhost:12344/stream"};

	     System.out.println("Streaming '" + media + "' to '" + options[0] + "'"); 

	     MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(); 
	     HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer(); 
	     mediaPlayer.playMedia(media, options[0], ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep");

	     // Don't exit
	     try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void recordData(byte data[]){
		try {
			if(count==120){
				count=0;
				makeDOS.write(data1);
				makeDOS.write(data2);
				makeDOS.write(data3);
			}			
			makeDOS.write(data);
			count++;
		} catch (IOException e) {
			recordEnd();
			e.printStackTrace();
		}				
	}
	public void recordEnd(){
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
}