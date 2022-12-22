package com.yuimorii.preparedstatement.preparedStatementUpdateTest.crud;

import com.yuimorii.statement.crud.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * @Description: 使用PreparedStatement實現針對於不同表的通用的查詢操作
 * @Author: yuimorii
 * @Param:
 * @return:
 * @Date:
 */

public class CustomerForQuery {

    @Test
    public void testQuery1() throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        //1.創建連結
        Connection connection = JDBCUtils.getConnection();
        //2. pre-compile sql statement
        String sql = "select id, name, birth, email from customers where id = ?";
        ps = connection.prepareStatement(sql);

        ps.setObject(1, 1);

        //执行,并返回结果集
        resultSet = ps.executeQuery();
        //处理结果集
        if (resultSet.next()) {//next():判断结果集的下一条是否有数据，如果有数据返回true,并指针下移；如果返回false,指针不会下移。

            //获取当前这条数据的各个字段值
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date birth = resultSet.getDate(4);

            //方式一：
//			System.out.println("id = " + id + ",name = " + name + ",email = " + email + ",birth = " + birth);

            //方式二：
//			Object[] data = new Object[]{id,name,email,birth};
            //方式三：将数据封装为一个对象（推荐）
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);

        }
    } catch(
    Exception e)

    {
        e.printStackTrace();
    }finally

    {
        //关闭资源
        JDBCUtils.closeResource(conn, ps, resultSet);

    }

}



