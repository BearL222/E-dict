package application.server;

import java.sql.*;
import java.util.ArrayList;

class DB {
	private String sqlName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String url = "jdbc:sqlserver://localhost:1433;DatabaseName=E_dict";
	private String user = "sa";
	private String password = "123456";

	DB() throws ClassNotFoundException {
		Class.forName(sqlName);
	}

	// 增加单词赞数
	public void setZan(int dict, String word) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM ZAN WHERE WORD = \'" + word + "\'");
		// 此时未存有该单词
		if (!resultSet.next()) {
			statement.executeUpdate("INSERT INTO ZAN(WORD,ZAN0,ZAN1,ZAN2)VALUES(\'" + word + "\',0,0,0)");
		}
		statement.executeUpdate("UPDATE ZAN SET ZAN" + dict + " = ZAN" + dict + " +1" + " WHERE WORD=\'" + word + "\'");
		connection.close();
	}

	// 取消赞
	public void unZan(int dict, String word) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM ZAN WHERE WORD = \'" + word + "\'");
		// 存有该单词
		if (resultSet.next()) {
			statement.executeUpdate(
					"UPDATE ZAN SET ZAN" + dict + " = ZAN" + dict + " -1" + " WHERE WORD=\'" + word + "\'");
		}
		connection.close();
	}

	// 返回该单词赞数
	public Integer getZan(int dict, String word) throws SQLException {
		int result;

		// 建立连接
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT ZAN" + dict + " FROM ZAN WHERE WORD = \'" + word + "\'");

		if (!resultSet.next()) {
			result = 0;
		} else {
			result = Integer.valueOf(resultSet.getString(1));
		}

		connection.close();
		return result;
	}

	// 用户登陆（返回：0：成功登陆；1：密码错误；2：用户不存在）
	public Integer signinUser(String name, String passcode) throws SQLException {
		int result = 0;
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM USER_INFO WHERE UNAME = \'" + name + "\'");

		if (resultSet.next()) {
			if (resultSet.getString(2).replace(" ", "").equals(passcode)) {
				// 登陆成功
				result = 0;
				statement.executeUpdate("UPDATE USER_INFO SET ONLINE = " + 1 + " WHERE UNAME = \'" + name + "\'");
			} else {
				// 密码错误
				result = 1;
			}

		} else {
			// 用户不存在
			result = 2;
		}
		connection.close();
		return result;
	}

	// 用户下线
	public Integer signoutUser(String name) throws SQLException {
		int result = 0;
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();
		statement.executeUpdate("UPDATE USER_INFO SET ONLINE = 0 WHERE UNAME = \'" + name + "\'");

		connection.close();
		return result;
	}

	// 用户注册（返回：0：注册成功；1：用户名已被占用）
	public Integer signupUser(String name, String passcode) throws SQLException {
		int result = 0;
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM USER_INFO WHERE UNAME = \'" + name + "\'");

		if (resultSet.next()) {
			// 该用户名已存在
			result = 1;
		} else {
			statement.executeUpdate(
					"INSERT INTO USER_INFO(UNAME,UCODE,ONLINE)VALUES(\'" + name + "\'," + passcode + ",0)");
			result = 0;
		}
		connection.close();
		return result;
	}

	// 返回所有在线用户，保存为String数组
	public String[] getOnlineUser() throws SQLException {

		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT UNAME FROM USER_INFO WHERE ONLINE = 1");
		ArrayList<String> result = new ArrayList<String>();

		while (resultSet.next()) {
			result.add(resultSet.getString(1).trim());
		}

		return result.toArray(new String[result.size()]);
	}

	// 返回所有离线用户，保存为String数组
	public String[] getOfflineUser() throws SQLException {

		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT UNAME FROM USER_INFO WHERE ONLINE = 0");
		ArrayList<String> result = new ArrayList<String>();

		while (resultSet.next()) {
			result.add(resultSet.getString(1).trim());
		}

		return result.toArray(new String[result.size()]);
	}

	// 获得某用户收到的单词
	public String[][] getShareWord(String name) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT UNAME1,WORD FROM WORD_CARD WHERE UNAME2 = \'" + name+"\'");
		ArrayList<String> result_sender = new ArrayList<String>();
		ArrayList<String> result_word = new ArrayList<String>();

		while (resultSet.next()) {
			result_sender.add(resultSet.getString(1));
			result_word.add(resultSet.getString(2));
		}
		String[][] result = new String[result_sender.size()][2];
		for (int i = 0; i < result_sender.size(); i++) {
			result[i][0] = result_sender.get(i).trim();
			result[i][1] = result_word.get(i).trim();
		}

		return result;
	}
	
	//用户发送单词卡
	public void setShareWord(String sender,String receiver,String word) throws SQLException{
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		statement.executeUpdate("INSERT INTO WORD_CARD(UNAME1,UNAME2,WORD)VALUES(\'"+sender+"\',\'"+receiver+"\',\'"+word+"\')");
	}
}
