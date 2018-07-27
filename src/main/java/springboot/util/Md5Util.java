package springboot.util;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String getMd5(String str,String salt) throws NoSuchAlgorithmException {

//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        return base64Encoder.encode(md5.digest(str.getBytes()));

        //加盐md5加密 散列2次
        return new Md5Hash(str,salt,2).toString();
    }
}
