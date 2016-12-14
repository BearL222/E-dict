package application.server;

import java.sql.*;

class DB {
	private String sqlName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String url = "jdbc:sqlserver://localhost:1433;DatabaseName=E_dict";
	private String user = "sa";
	private String password = "123456";

	DB() throws ClassNotFoundException {
		Class.forName(sqlName);
	}

	// 修改单词赞数信息
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

	// 返回该单词赞数
	public int getZan(int dict, String word) throws SQLException {
		int result;

		// 建立连接
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT ZAN" + dict + " FROM ZAN WHERE WORD = \'" + word + "\'");

		if (!resultSet.next()) {
			result = -1;
		} else {
			result = Integer.valueOf(resultSet.getString(1));
		}

		connection.close();
		return result;
	}

	// 用户登陆（返回：0：成功登陆；1：密码错误；2：用户不存在）
	public int signinUser(String name, String passcode) throws SQLException {
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

	//用户下线
	public int signoutUser(String name) throws SQLException {
		int result = 0;
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement statement = connection.createStatement();
		statement.executeUpdate("UPDATE USER_INFO SET ONLINE = 0 WHERE UNAME = \'" + name + "\'");

		connection.close();
		return result;
	}
	
	// 用户注册（返回：0：注册成功；1：用户名已被占用）
	public int signupUser(String name, String passcode) throws SQLException {
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
}