package com.yuimorii.preparedstatement.preparedStatement.crud;


import com.yuimorii.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 使用preparedstatement來替換statemnt，實現對數據的CRUD操作
 */
public class PreparedStatementUpdateTest {
    @Test
    public void testCommonUpdate() {
//        String sql = "delete from customers where id = ?";
//        update(sql, 3);
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql,"DD","2");

    }
    //Generic CRUD operations



    public void update(String sql, Object... args) {
        //The number of placeholders in /sql is the same as the length of the deformable parameter!
        Connection connection = null;
        PreparedStatement psInstance = null;
        try {
            //1.Get the connection to the database
            connection = JDBCUtils.getConnection();
            //2. Pre-compile sql statements to return instances of PreparedStatement
            psInstance = connection.prepareStatement(sql);
            //3.Fill Placeholder 填充佔位符
            for (int i = 0; i < args.length; i++) {
                psInstance.setObject(i + 1, args[i]);
            }
            //4.Execution
            psInstance.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.Closure of resources
            JDBCUtils.closeResource(connection, psInstance);
        }
    }

    @Test
    //modify records in customer table
    public void TestUpdate() throws Exception {

        Connection connection = null;
        PreparedStatement psInstance = null;
        //1.Connection with database
        try {
            connection = JDBCUtils.getConnection();


            //2.Pre-compile sql statements to return instances of PreparedStatement
            String sql = "update customers set name = ? where id = ? ";
            psInstance = connection.prepareStatement(sql);
            //3.Fill placeholders
            psInstance.setObject(1, "Matthew");
            psInstance.setObject(2, 18);
            //4.Execution
            psInstance.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.Close resources
            JDBCUtils.closeResource(connection, psInstance);
        }
    }

    // create a row in the customers table
    @Test
    public void TestInsert() throws Exception {

        Connection connection = null;
        PreparedStatement psInstance = null;

        try {
            //1. Read the 4 basic information in the configuration file
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

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
            //System.out.println(connnection);
            //com.mysql.cj.jdbc.ConnectionImpl@30ee2816

            //4. Pre-compile sql statements to return instances of PreparedStatement
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//?:placeholder
            psInstance = connnection.prepareStatement(sql);
            //5. Fill placeholders
            psInstance.setString(1, "MatthewOS");
            psInstance.setString(2, "MatthewOS@gmail.com");
            //對日期特殊parse方法解析
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("2000-01-01");
            psInstance.setDate(3, (java.sql.Date) new Date(date.getTime()));
            //6.Execution
            psInstance.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.资源的关闭
            try {
                if (psInstance != null)
                    psInstance.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
