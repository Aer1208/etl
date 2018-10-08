package com.mongohua.etl.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Md5加密算法
 * @author xiaohf
 */
public class Md5Utils {

    public static  String encode(String str, int hashIterations) {
        //加密方式
        String hashAlgorithmName = "MD5";
        //盐值
        Object salt = null;
        Object result = new SimpleHash(hashAlgorithmName,str,salt,hashIterations);
        return result.toString();
    }
}
