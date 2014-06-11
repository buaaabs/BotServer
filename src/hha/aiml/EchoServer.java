package hha.aiml;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class EchoServer implements Runnable {

	protected ServerSocket svr = null;
	protected int port;
	public boolean isServerRun = true;
	
	public EchoServer(int i) {
		// TODO Auto-generated constructor stub
		port = i;
	}

	public void run()
	{
		try {
			   System.out.println("Server Run!");

			   svr = new ServerSocket(port);
	           System.out.println("Socket OK!");
	           
	           SyncQueue queue = new SyncQueue(100);
	           for (int i = 0; i < 15; i ++) {
	              Thread t = new Thread(new Worker(queue));
	              t.setDaemon(true);
	              t.start();
	           }
	           System.out.println("Thread Pool Start!");
	           
	           while (isServerRun) {
	              Socket sock = svr.accept();
	              queue.put(new EchoSession(sock));
	              System.out.println("accept!");
	           }
	           
	       } catch (IOException | ExceptionAdapter ex) {
	    	   System.out.println("Server Failed!");
	    	   ex.printStackTrace();
	       }
	}
}
