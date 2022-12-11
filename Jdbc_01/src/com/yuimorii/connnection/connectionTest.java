package com.yuimorii.connnection;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class connectionTest {
    @Test
    public void Test01() {
        try {
            //1.提供java.sql.Driver接口实现类的对象
            Driver driver = null;
            driver = new com.mysql.jdbc.Driver();

            //2.提供url，指明具体操作的数据
            String url = "jdbc:mysql://localhost:3306/test";

            //3.提供Properties的对象，指明用户名和密码
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "yuimorii");

            //4.调用driver的connect()，获取连接

            Connection connection = driver.connect(url, info);
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


