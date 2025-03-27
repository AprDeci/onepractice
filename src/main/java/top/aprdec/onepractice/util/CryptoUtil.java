package top.aprdec.onepractice.util;

import cn.dev33.satoken.secure.SaSecureUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoUtil {
    //    使用AES加密
    final static String key = "Maybewewonseethesunrise";

    public static String encrypt(String text) {
        return SaSecureUtil.aesEncrypt(key,text);
    }

    public static String decrypt(String text) {
        return SaSecureUtil.aesDecrypt(key,text);
    }

}
