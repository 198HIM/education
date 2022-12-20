package com.voup.education.entity;

import lombok.Data;

/**
 * @Author HIM198
 * @Date 2022 13:04
 * @Description
 **/


public class LoginPower {
    private String name;
    private String password;
    private String tCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
    }
}
