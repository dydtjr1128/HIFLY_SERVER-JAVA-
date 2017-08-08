import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.codecs.h264.H264Decoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.common.ByteBufferSeekableByteChannel;
import org.jcodec.common.JCodecUtil;
import org.jcodec.common.VideoDecoder;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

public class ServiceThread extends Thread {
	private Socket socket;
	private BufferedReader reader;
	private DataInputStream inputStream;
	private ShowFrame sf;
	H264Decoder decoder = null;

	public ServiceThread(Socket socket) {
		this.socket = socket;
		try {			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inputStream = new DataInputStream(socket.getInputStream());
			int size = inputStream.readInt();
			System.out.println(size + "¹ÞÀ½");			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		sf = new ShowFrame();	
		if (decoder == null)
			 decoder = new H264Decoder();
		while(true){
			try {
				int size = inputStream.readInt();				
				byte readByte[] = new byte[size];
				inputStream.readFully(readByte, 0, size);				
				ByteBuffer data = ByteBuffer.wrap(readByte);
				Picture out = Picture.create(1280, 720, ColorSpace.YUV420); // Allocate output frame of max size				
			    Picture pic = decoder.decodeFrame(data, out.getData());
			    
				BufferedImage img = AWTUtil.toBufferedImage(pic);
				sf.setBufferedImage(img);				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
