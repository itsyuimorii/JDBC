package com.yuimorii._3.preparedstatement.preparedStatement.crud;

import com.yuimorii._3.preparedstatement.bean.Customer;
import com.yuimorii._3.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;


public class CustomerForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select id,name,birth,email from customers where id = ?";
        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);

        sql = "select name,email from customers where name = ?";
        Customer customer1 = queryForCustomers(sql,"周杰伦");
        System.out.println(customer1);
    }

    /**
     * @Description: 針對customer表通用的查詢操作
     * @Author: yuimorii
     * @Param: [sql, args]
     * @return:
     * @throws: Exception
     * @Date:
     */

    @Test
    public Customer queryForCustomers(String sql, Object... args)  {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection connection = JDBCUtils.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //fill placeholder
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //返回結果集
            rs = ps.executeQuery();
            //获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) { //If表示對接到了某一行
                //用空參構造器造一個對象
                Customer customer = new Customer();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columValue = rs.getObject(i + 1);

                    //获取每个列的列名（反射的應用）
                    //String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给cust对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columValue);
                }
                return customer;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    /**
     * @Description: 使用PreparedStatement實現針對於不同表的通用的查詢操作
     * @Author: yuimorii
     */

    @Test
    public void testQuery1() throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //1.創建連結
            Connection connection = JDBCUtils.getConnection();
            //2. pre-compile sql statement
            String sql = "select id, name, email,birth from customers where id = ?";
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
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(conn, ps, resultSet);

        }

    }
}



