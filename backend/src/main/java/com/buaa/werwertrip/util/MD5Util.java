package com.buaa.werwertrip.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    // 进行MD5加密
    public static String md5(String pwd, String salt) {
        pwd = salt.charAt(0) + salt.charAt(3) + pwd + salt.charAt(6) + salt.charAt(1);
        pwd = salt.charAt(1) + salt.charAt(3) + pwd + salt.charAt(6) + salt.charAt(1);
        pwd = DigestUtils.md5Hex(pwd);
        return pwd;
    }

}
