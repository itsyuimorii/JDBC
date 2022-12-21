package com.yuimorii.statement.crud;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import com.yuimorii.statement.crud.util.JDBCUtils;
import org.junit.Test;

/**
 * @Description 演示使用PreparedStatement替换Statement，解决SQL注入问题
 * @date
 */
public class PreparedStatementTest {
    @Test
    public void testLogin() {
//get users input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please Enter Your Username");
        String user = scanner.nextLine();
        System.out.print("Please Enter Your Password：");
        String password = scanner.nextLine();
        //SELECT user,password
        //FROM user_table WHERE user = "AA" and password = "123456";
        String sql = "SELECT user,password FROM user_table WHERE user = ? and password = ?";
        User returnUser = getInstance(User.class, sql, user, password);
        if (returnUser != null) {
            System.out.println("Successful! Welcome back....");
        } else {
            System.out.println("Username does not exist or password is wrong");
        }
    }

    /**
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @Description 针对于不同的表的通用的查询操作，返回表中的一条记录
     * @author
     * @date
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps, rs);

        }

        return null;
    }
}
