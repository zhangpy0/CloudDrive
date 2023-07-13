package com.zhangpy.CloudDrive.util;

public class StringJudge {
    public static boolean isInRange(String str) {
        if (str == null) return false;
        return str.length() >= 5 && str.length() <= 20;
    }

    public static boolean isEmail(String str) {
        if (str == null) return false;
        return str.contains("@") && str.contains(".") && str.length() <= 30;
    }
}
