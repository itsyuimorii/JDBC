package com.yuimorii.statement.crud.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 
 * @Description 操作数据库的工具类
 * @author shkstart Email:shkstart@126.com
 * @version
 * @date 上午9:10:02
 *
 */
public class JDBCUtils {
	
	/**
	 * 
	 * @Description get connection from DB
	 * @date
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		// 1.读取配置文件中的4个基本信息
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		// 2.加载驱动
		Class.forName(driverClass);

		// 3.获取连接
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}

	/**
	 * @Description 关闭连接和Statement的操作
	 * @param connection
	 * @param psInstance
	 */
	public static void closeResource(Connection connection, Statement psInstance){
		try {
			if(psInstance != null)
				psInstance.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description 关闭资源操作
	 * @author shkstart
	 * @date 上午10:21:15
	 * @param connection
	 * @param psInstance
	 * @param resultSet
	 */
	public static void closeResource(Connection connection,Statement psInstance,ResultSet resultSet){
		try {
			if(psInstance != null)
				psInstance.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(resultSet != null)
				resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
