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
		// 是否已经存有该单词
		if (!resultSet.next()) {
			statement.executeUpdate("INSERT INTO ZAN(WORD,ZAN0,ZAN1,ZAN2)VALUES(\'" + word + "\',0,0,0)");
		}
		statement.executeUpdate("UPDATE ZAN SET ZAN" + dict + " = ZAN" + dict + " +1" + " WHERE WORD=\'" + word + "\'");
		connection.close();

	}

	// 获得该单词赞数
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
}
