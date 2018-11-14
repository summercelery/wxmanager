package springboot.core.util;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String getMd5(String str,String salt){
        //加盐md5加密 散列2次
        return new Md5Hash(str,salt,2).toString();
    }
}
