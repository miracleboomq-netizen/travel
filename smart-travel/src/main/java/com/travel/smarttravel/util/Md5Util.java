package com.travel.smarttravel.util;

import org.springframework.util.DigestUtils;

public class Md5Util {
    // 🌟 盐值固定为：smart_travel_666
    private static final String SALT = "smart_travel_666";

    public static String encode(String password) {
        if (password == null) return null;
        // 逻辑：明文密码 + 盐值 -> MD5
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }
}