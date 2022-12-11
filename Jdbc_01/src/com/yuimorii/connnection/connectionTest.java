package com.yuimorii.connnection;

import org.junit.Test;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

public class connectionTest {
    @Test
    public void Test01() {
        try {
            // 1. Provide objects of the java.sql.Driver interface implementation class
            Driver driver = null;
            // url:http://localhost:8080/gmall/keyboard.jpg
            // jdbc:mysql:协议
            // localhost:ip地址
            // 3306：默认mysql的端口号
            // test:test数据库
            driver = new com.mysql.jdbc.Driver();
            //2. Provide the url, specifying the data for the specific operation
            String url = "jdbc:mysql://localhost:3306/test";
            //3. Provide the object of Properties, specifying the username and password
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "yuimorii");
            //4. Call driver's connect() to get the connection
            Connection connection = driver.connect(url, info);
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method 2: Iteration of Method 1: no third-party api in the following program, making the program with better portability
    @Test
    public void Test02() throws Exception {
        try {
            // 1. Get the Driver implementation class object: use reflection
            Class clazz = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) clazz.newInstance();
            // 2. Provide the database to be connected to
            String url = "jdbc:mysql://localhost:3306/test";
            // 3. Provide the username and password needed to connect
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "yuimorii");
            // 4. Get the connection
            Connection connection = driver.connect(url, info);
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method 3: Use DriverManager to replace Driver
    public void Test03() throws Exception {
        // 1. Get the Driver implementation class object: use reflection
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        // 2. Provide basic information about the other three connections.
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "yuimorii";
        // Register the driver
        DriverManager.registerDriver(driver);
        //Get Connected
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //Method 4:  Load the driver, without having to show the registered driver over.
    @Test
    public void Test04() throws Exception {
        // 1. Provide basic information about the three connections.
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "yuimorii";
        // 2. Load Driver
        Class.forName("com.mysql.jdbc.Driver");
        // Compared to mode 3, the following operations can be omitted.
	    /*
	     * Why can the above operation be omitted?
		 * In the Driver implementation class of mysql, the following operation is declared.
		 * static {
				try {
					java.sql.DriverManager.registerDriver(new Driver());
				} catch (SQLException E) {
					throw new RuntimeException("Can't register driver!");
				}
			}
		 */
        //Get Connected
        Connection connnection = DriverManager.getConnection(url, user, password);
        System.out.println(connnection);
    }
    //Method 5 (final version): declare the 4 basic information needed for database connection in the configuration file, and get the connection by reading the configuration file
    /*
     * The benefits of this approach?
     * 1. Separation of data and code is achieved. Achieves decoupling
     * 2. If you need to modify the configuration file information, you can avoid repackaging the program.
     */
    @Test
    public void Test05() throws Exception{

        //1. Read the 4 basic information in the configuration file
        InputStream is = connectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        // 2. Load Driver
        Class.forName(driverClass);

        //3.get connected
        Connection connnection = DriverManager.getConnection(url, user, password);
        System.out.println(connnection);
    }
}

