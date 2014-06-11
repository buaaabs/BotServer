package hha.aiml;

import hha.aiml.Tuple.Tuple2;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQL {
	// 驱动程序名
	static String driver = "com.mysql.jdbc.Driver";

	// URL指向要访问的数据库名AliceBot
	static String url = "jdbc:mysql://127.0.0.1:3306/AliceBot?characterEncoding=UTF8";

	// MySQL配置时的用户名
	static String user = "sxf";

	// MySQL配置时的密码
	static String password = "12315";
	static Statement statement;
	static Connection conn;

	public static void initDatabase() {
		// 加载驱动程序
		try {
			Class.forName(driver);

			// 连接数据库
			conn = DriverManager.getConnection(url, user, password);

			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");

			// statement用来执行SQL语句
			statement = conn.createStatement();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Sorry,can't find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void CloseConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int AddCategory(String str) {
		String sql = "insert into aiml VALUES (NULL, '" + str + "');";
		String sql2 = "select LAST_INSERT_ID();";
		ResultSet rs;
		int ans = 0;
		try {
			statement.executeUpdate(sql);

			rs = statement.executeQuery(sql2);

			while (rs.next()) {

				ans = rs.getInt(1);
			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}

	public static int AddCategory(int id, String str) {
		String sql = "insert into aiml VALUES (" + id + ", '" + str + "');";

		try {
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public static void UpdateCategory(int id, String str) {
		String sql = "Update aiml Set data='" + str + "' Where id="+id+";";

		try {
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void DeleteCategory(int id) {
		String sql = "Delete from aiml Where id="+id+";";

		try {
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String FindCategory(int id) {
		String sql = "select data from aiml where id='" + String.valueOf(id)
				+ "';";

		ResultSet rs;
		String ans = "";
		try {
			rs = statement.executeQuery(sql);

			while (rs.next()) {

				ans = rs.getString(1);
			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}

	public static List<Tuple2<Integer, String>> ListAllCategory() {
		// 要执行的SQL语句
		String sql = "select * from aiml;";

		// 结果集
		ResultSet rs;
		List<Tuple2<Integer, String>> data = new ArrayList<Tuple2<Integer, String>>();

		try {
			rs = statement.executeQuery(sql);

			while (rs.next()) {

				Tuple2<Integer, String> tp = Tuple.tuple(rs.getInt("id"), rs
						.getString("data").trim());
				data.add(tp);

			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * For Test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initDatabase();

		System.out.println("-----------------");
		System.out.println("执行结果如下所示:");
		System.out.println("-----------------");
		System.out.println(" id" + "\t" + " category");
		System.out.println("-----------------");

		List<Tuple2<Integer, String>> data = ListAllCategory();
		for (Tuple2<Integer, String> i : data) {
			System.out.print(i._1());
			System.out.println("\t" + i._2());
		}

		System.err.println(FindCategory(3));

//		System.err
//				.println(AddCategory(
//						4,
//						"<category>   <pattern>OK</pattern>  <template>    <random>    <li>诺</li>      <li>喳</li>      <li>好，这就播</li>      <li>Yes,sir</li>    <li>为人民服务</li>     <li>为您服务是我的荣幸</li><li>得令</li><li>嗨</li><li>OK</li></random></template></category> "));
		DeleteCategory(4);
	}

}
