package hha.aiml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

public class EchoSession {

	public Socket sock = null;

	public EchoSession(Socket _sock) {
		// TODO Auto-generated constructor stub
		sock = _sock;
	}
	
	
	
	public void run() throws ExceptionAdapter {
		// TODO Auto-generated method stub
		try {
			try {
				InputStream input = sock.getInputStream();
				OutputStream output = sock.getOutputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(input,"utf-8"));
				PrintStream ps = new PrintStream(output);
				String str = "";
				str = bufferedReader.readLine();
				if (!str.equals("BEGIN")) {
					throw new Exception("Error:No BEGIN header");
				}
				while (!(str = bufferedReader.readLine()).equals("END")) {	
//					TODO: 分析commands并执行相应的函数
					System.err.println(str);
					Server.Command(str, bufferedReader,ps);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sock.close();
			}
		} catch (IOException ex) {
			throw new ExceptionAdapter(ex);
		}

	}

}
