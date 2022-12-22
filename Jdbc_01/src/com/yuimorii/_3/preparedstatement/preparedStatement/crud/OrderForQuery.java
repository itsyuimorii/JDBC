package com.yuimorii._3.preparedstatement.preparedStatement.crud;

import com.yuimorii._3.preparedstatement.bean.Order;
import com.yuimorii._3.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.sql.*;

/**
 * @program: JDBC
 * @description: 针对于表的字段名与类的属性名不相同的情况：
 * * 1. 必须声明sql时，使用类的属性名来命名字段的别名
 * * 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),
 * *    获取列的别名。
 * *  说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名
 * @author: yuimorii
 * @create: 2022-12-21 20:50
 **/
public class OrderForQuery {
    @Test
    public void testQuery() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.getConnection();
            String sql = "SELECT order_id, order_name, order_date FROM `order` where order_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, 1);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = (int) resultSet.getObject(1);
                String name = (String) resultSet.getObject(2);
                Date date = (Date) resultSet.getObject(3);
                Order order = new Order(id, name, date);
                System.out.println(order);


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }


    }
}

