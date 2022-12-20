package com.voup.education.utils;

/**
 * @Author HIM198
 * @Date 2022 14:42
 * @Description
 **/

public class StringNumber {
    public int getStringNumber(String str) {
        int number = 0;
        char[] chars = str.toCharArray();
        for (char c:chars
             ) {
            number++;
        }
        return number;
    }
}
