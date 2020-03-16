package cn.lixing.conn.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static cn.lixing.uilts.PropertiesFileUilt.*;
import static cn.lixing.uilts.CloseUilt.*;

public class ConnectDataBase {
	private static Connection connection;
	private static String sep=System.getProperty("file.separator");
	public static Connection getConnect(){
		try {
			Class.forName(getData("mysqldriverClass", sep+"configFile"+sep+"db"));
			connection=DriverManager.getConnection(
					getData("mysqlurl", sep+"configFile"+sep+"db"), 
					getData("mysqlusername", sep+"configFile"+sep+"db"), 
					getData("mysqlpassword", sep+"configFile"+sep+"db"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			closeConnection(connection);
			e.printStackTrace();
		}
		return connection;
	}
	public static void main(String[] args) {
		getConnect();
	}
}
