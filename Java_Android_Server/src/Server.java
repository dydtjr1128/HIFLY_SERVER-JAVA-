import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
	private ServerSocket serverSocket;
	private Socket socket;
	public Server() {
		init();
		new Thread(this).start();
		System.out.println("서버 시작!");
	}
	public void init(){
		try {
			serverSocket = new ServerSocket(10123);		
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	public void run(){
		while(true){
			try {
				System.out.println("대기중!");
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + "  " + socket.getLocalAddress() + "접속!");
				ServiceThread st = new ServiceThread(socket);
				st.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
