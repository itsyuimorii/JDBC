package com.yuimorii._1.connnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
实现功能：
    1、需求：
        模拟用户登录功能的实现。
    2、业务描述：
        -程序运行的时候，提供一个输入的入口，可以让用户输入用户名和密码
        -用户输入用户名和密码之后，提交信息，java程序收集到用户信息
        -Java程序连接数据库验证用户名和密码是否合法
        ---合法：显示登录成功
        ---不合法：显示登录失败
    3、数据的准备：
        在实际开发中，表的设计会使用专业的建模工具，我们这里安装一个建模工具：PowerDesigner
        使用PD工具来进行数据库表的设计。（参见user-login.sql脚本）
    4、当前程序存在的问题：
        用户名：fdsa
        密码：fdsa' or '1'='1
        登录成功
        这种现象被称为SQL注入(安全隐患)。（黑客经常使用）
    5、导致SQL注入的根本原因是什么？
        用户输入的信息中含有sql语句的关键字，并且这些关键字参与sql语句的编译过程，
        导致sql语句的原意被扭曲，进而达到sql注入。
 */
public class connectionTest06 {
    public static void main(String[] args) {
        //初始化界面:定義一個方法去完成頁面初始化
        //這裡應該需要用map接收返回值-
        Map<String, String> userLoginInfo = initUI();

        //驗證用戶名和密碼
        boolean loginSuccess = login(userLoginInfo);
        //輸出結果
        System.out.println(loginSuccess ? "successful" : "failed,please try again");
    }

    /**
     * @Description:
     * @Author: yuimorii
     * @Param: userLoginInfo 用戶登陸信息
     * @return:
     * @Date:
     */
    private static boolean login(Map<String, String> userLoginInfo) {
        //打標機
        boolean loginSuccess = false;

        // 单独定义变量
        String loginName = userLoginInfo.get("loginName");
        String loginPwd = userLoginInfo.get("loginPwd");


        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //1. 註冊驅動
            Class.forName("com.mysql.jdbc.Driver");
            //2. 獲取連結
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "yuimorii");
            //3. 獲取數據庫操作對象
            stmt = connection.createStatement();
            //4. 執行sql
            String sql = "select * from t_user where loginName = '"+loginName+"' and loginPwd = '"+loginPwd+"'";
            // 5. 處理結果集
            //while(rs.next()不需要用循環
            // 以上正好完成了sql语句的拼接，以下代码的含义是，发送sql语句给DBMS，DBMS进行sql编译。
            // 正好将用户提供的“非法信息”编译进去。导致了原sql语句的含义被扭曲了。
            rs = stmt.executeQuery(sql);
            // 5、处理结果集
            if(rs.next()){
                // 登录成功
                loginSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6、释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return loginSuccess;
    }

    /**
     * @return
     * @Description 初始化用戶界面
     */
    private static Map<String, String> initUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("username: ");
        String loginName = scanner.nextLine();

        System.out.println("password: ");
        String loginPwd = scanner.next();

        Map<String, String> userLogInfo = new HashMap<>();
        userLogInfo.put("loginName", loginName);
        userLogInfo.put("loginPwd", loginPwd);


        return userLogInfo;
    }
}
