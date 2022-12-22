package com.yuimorii._3.preparedstatement.bean;

import java.sql.Date;

/**
 * @program: JDBC
 * @description: ORM object relational mapping對象關係映射
                       * 一个数据表对应一个java类
                       * 表中的一条记录对应java类的一个对象
                       * 表中的一个字段对应java类的一个属性
 * @author: yuimorii
 * @create: 2022-12-21 19:56
 **/

public class Customer {

    private int id;
    private String name;
    private String email;
    private Date birth;
    public Customer() {
        super();
    }
    public Customer(int id, String name, String email, Date birth) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirth() {
        return birth;
    }
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", birth=" + birth + "]";
    }



}
