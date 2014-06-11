package hha.aiml;

import hha.aiml.Tuple.Tuple2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

	static Robot bot = new Robot();
	static EchoServer server = new EchoServer(10010);
	static boolean isServerDone = false;
	static Thread ServerThread = null;

	static Map<String, Robot> botmap = new HashMap<String, Robot>();

	public static int FindCategory(String str) {
		return bot.Find(str);
	}

	public static void Learn(String str) {
		try {
			bot.LearnFromStream(0,
					new ByteArrayInputStream(str.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String Respond(String id, String str) {
		Robot robot = null;
		if (!botmap.containsKey(id)) {
			robot = new Robot(bot);
			botmap.put(id, robot);
		} else {
			robot = botmap.get(id);
		}
		return robot.Respond(str);
	}

	public static void RestartServer() {
		StopServer();
		System.err.println("Server Restarting......");
		StartServer();
	}

	public static InputStream StringTOInputStream(String in) {  
        
        ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(in.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return is;  
    }  
	
	public static void StartServer() {
		System.out.println("Server Starting......");
		bot.InitRobot();
		System.out.println("Robot Init Done!");
		SQL.initDatabase();
		List<Tuple2<Integer, String>> list = SQL.ListAllCategory();
		for (Tuple2<Integer, String> i : list)
		{
			bot.LearnFromStream(i._1(),StringTOInputStream( i._2()));
		}
		System.out.println("Database Init Done!");
		ServerThread = new Thread(server);
		ServerThread.setDaemon(true);
		ServerThread.start();
	}

	public static void StopServer() {
		try {
			server.svr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void CloseServer() {
		isServerDone = true;
	}

	static String getArgs(BufferedReader br) throws IOException {
		if (!br.readLine().equals("BEGIN{"))
			return null;
		StringBuilder sb = new StringBuilder("");
		String str = "";
		while (!(str = br.readLine()).equals("}END")) {
			sb.append(str);
		}
		System.err.println(sb.toString());
		return sb.toString();
	}

	public static void Command(String command, BufferedReader br, PrintStream ps) {
		// System.out.println("Command:" + command);
		if (command.equals("Start")) {
			Server.StartServer();
			ps.println("True");
			return;
		} else if (command.equals("Stop")) {
			Server.StopServer();
			ps.println("True");
			return;
		} else if (command.equals("Close")) {
			Server.CloseServer();
			ps.println("True");
			return;
		} else if (command.equals("Restart")) {
			Server.RestartServer();
			ps.println("True");
			return;
		} else if (command.equals("ListAll")) {
			List<Tuple2<Integer, String>> ans = SQL.ListAllCategory();
			StringBuilder sb = new StringBuilder();
			for (Tuple2<Integer, String> i : ans) {
				sb.append("#");
				sb.append(i._1());
				sb.append("\n");
				sb.append(i._2());
				sb.append("\n");
			}
			ps.println(sb.toString());
		}

		String arg = "";
		try {
			arg = Server.getArgs(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ps.println("Arg1 Error");
		}

		if (command.equals("Find")) {
			int k = -1;
			k = Integer.parseInt(arg);
			String ans = SQL.FindCategory(k);
			ps.println(ans);
			ps.println("END");
			System.out.println(ans);
			return;
		} else if (command.equals("FindByString")) {
			int k = FindCategory(arg);
			ps.println(k);
			ps.println("END");
			System.out.println(k);
			return;
		} else if (command.equals("Learn")) {
			Server.Learn(arg);
			SQL.AddCategory(arg);
			ps.println("True");
			return;
		} else if (command.equals("Add")) {
			SQL.AddCategory(arg);
			ps.println("True");
			return;
		} else if (command.equals("Delete")) {
			int k = -1;
			k = Integer.parseInt(arg);
			SQL.DeleteCategory(k);
			return;
		}

		String arg2 = "";

		try {
			arg2 = Server.getArgs(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ps.println("Arg2 Error");
		}

		if (command.equals("Update")) {
			int k = -1;
			k = Integer.parseInt(arg);
			SQL.UpdateCategory(k, arg2);
			return;
		} else if (command.equals("Insert")) {
			int k = -1;
			k = Integer.parseInt(arg);
			SQL.AddCategory(k, arg2);
			return;
		} else if (command.equals("Respond")) {
			String ans = Server.Respond(arg, arg2);
			ps.println(ans);
			System.err.println(ans);
			return;
		}

		ps.println("Can't Find Command");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StartServer();

		String command = "";
		while (!isServerDone) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				command = br.readLine();
				Command(command, br, System.err);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
