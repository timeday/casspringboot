package com.cas.authentication;

import java.io.Serializable;

/**
 * @Project casspringboot
 * @Package com.cas.authentication
 * @ClassName Userinfo
 * @Descripition TODO
 * @Author able
 * @Date 2019/6/13 19:48
 * @Version 1.0
 **/
public class Userinfo implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String name;
    private String idCardNum;
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", idCardNum='" + idCardNum + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
