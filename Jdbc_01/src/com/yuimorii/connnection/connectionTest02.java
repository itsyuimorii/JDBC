package com.yuimorii.connnection;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class connectionTest02 {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            //1. 註冊驅動
            // Driver driver = new oracle.jdbc.driver.OracleDriver(); // oracle的驱动。
            Driver driver = new com.mysql.jdbc.Driver(); // 多态，父类型引用指向子类型对象。
            DriverManager.registerDriver(driver);
            /*
             擴展url知識：
            	URL包括哪几部分？
					协议
					IP
					PORT
					资源名
				什么是通信协议，有什么用？
					通信协议是通信之前就提前定好的数据传送格式。
					数据包具体怎么传数据，格式提前定好的。
             */
            //2. 獲取連接
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";
            String password = "yuimorii";

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("数据库连接对象 = " + connection);
            //数据库连接对象 = com.mysql.cj.jdbc.ConnectionImpl@436a4e4b


            //3、获取数据库操作对象(Statement专门执行sql语句的)
            statement = connection.createStatement();

            //4、执行sql
            String sql = "insert into dept(deptno,dname,loc) values(50,'人事部','北京')";
            // 专门执行DML语句的(insert delete update)
            // 返回值是“影响数据库中的记录条数”
            int count = statement.executeUpdate(sql);
            System.out.println(count == 1 ? "保存成功" : "保存失败");

            //5、处理查询结果集

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //6、释放资源
            // 为了保证资源一定释放，在finally语句块中关闭资源
            // 并且要遵循从小到大依次关闭
            // 分别对其try..catch
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}