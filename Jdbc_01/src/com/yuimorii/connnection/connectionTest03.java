package com.yuimorii.connnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class connectionTest03 {
    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        try {
            //1. 註冊驅動
            //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //註冊驅動的第二種寫法 为什么这种方式常用？（参数是一个字符串， 可以寫到配置文件中xxx.properties，因為參數是一個字符串）
            // 以下方法不需要接收返回值，因为我们只想用它的类加载动作。
            Class.forName("new com.mysql.jdbc.Driver()");
            //2. 獲取連結
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "yuimorii");
            //3. 獲取數據庫操作對象
            statement = connection.createStatement();
            //4. 執行sql 語句
            String sql = "delete from dept where deptno= 40";
            int count = statement.executeUpdate(sql);
            System.out.println(count == 1 ? "successful" : "failed");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (connection != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
