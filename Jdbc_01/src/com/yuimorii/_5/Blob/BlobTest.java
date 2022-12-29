package com.yuimorii._5.Blob;

import com.yuimorii._3.preparedstatement.bean.Customer;
import com.yuimorii._3.preparedstatement.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @program: JDBC
 * @Description 测试使用PreparedStatement操作Blob类型的数据
 * @author: yuimorii
 * @create: 2022-12-22 11:28
 **/
public class BlobTest {
    //向數據表customers中插入blob類型的字段
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement psInstance =null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
             psInstance = connection.prepareStatement(sql);
            //填充佔位符
            psInstance.setObject(1, "Matt");
            psInstance.setObject(2, "Matt@Matt.com");
            psInstance.setObject(3, "1992-09-08");
            //填充（圖片格式）
            FileInputStream is = new FileInputStream(new File("imgtest.jpeg"));
            psInstance.execute();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, psInstance);
        }

    }

    //查询数据表customers中Blob类型的字段
    @Test
    public void testQuery(){
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 21);
            rs = ps.executeQuery();
            if(rs.next()){
                //			方式一：
                //			int id = rs.getInt(1);
                //			String name = rs.getString(2);
                //			String email = rs.getString(3);
                //			Date birth = rs.getDate(4);
                //方式二：
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer cust = new Customer(id, name, email, birth);
                System.out.println(cust);

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("zhangyuhao.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCUtils.closeResource(conn, ps, rs);
        }


    }

}


