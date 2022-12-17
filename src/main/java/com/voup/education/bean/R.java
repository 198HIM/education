package com.voup.education.bean;

import lombok.Data;

/**
 * @Author HIM198
 * @Date 2022 0:01
 * @Description
 **/

@Data
public class R {
    private int code= 0;  //编码：0成功，1和其它数字为失败
    private String msg = "";
    private Object data = null;

    public static R success(Object object) {
        R r = new R();
        r.code=0;
        r.data=object;
        return r;
    }
    public static R success(Object object,String msg) {
        R r = new R();
        r.code=0;
        r.data=object;
        r.msg=msg;
        return r;
    }
    public static R error(String msg) {
        R r = new R();
        r.code=1;
        r.msg=msg;
        return r;
    }
    @Override
    public String toString() {
        return "R [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
