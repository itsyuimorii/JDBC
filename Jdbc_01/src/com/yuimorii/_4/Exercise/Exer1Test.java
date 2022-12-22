package com.yuimorii._4.Exercise;

import com.yuimorii._3.preparedstatement.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.junit.Test;


/**
 * @program: JDBC
 * @description: 从控制台向数据库的表customers中插入一条数据，表结构如下
 * @author: yuimorii
 * @create: 2022-12-22 10:11
 **/
public class Exer1Test {

    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入邮箱：");
        String email = scanner.next();
        System.out.print("请输入生日：");
        String birthday = scanner.next();//'1992-09-08'

        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int insertCount = update(sql, name, email, birthday);
        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }


    //通用的增刪改查操作
    public int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement psInstance = null;
        //1. 獲取數據庫的連接
        try {
            connection = JDBCUtils.getConnection();
            //2. pre-compile sql statement，返回PreparedStatement實例
            psInstance = connection.prepareStatement(sql);
            //3. 填充站位符
            for (int i = 0; i < args.length; i++) {
                psInstance.setObject(i + 1, args[i]);
            }
            // 4.执行
            /*
             * psInstance.execute():
             * 如果执行的是查询操作，有返回结果，则此方法返回true;
             * 如果执行的是增、删、改操作，没有返回结果，则此方法返回false.
             */
            //方式一：
            //return psInstance.execute();
            //方式二：
            return psInstance.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, psInstance);
        }
        return 0;
    }
}

